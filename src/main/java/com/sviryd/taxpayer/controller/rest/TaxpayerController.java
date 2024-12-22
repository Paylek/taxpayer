package com.sviryd.taxpayer.controller.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sviryd.taxpayer.config.TaxpayerConfig;
import com.sviryd.taxpayer.domain.vo.TaxpayerVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/egr")
public class TaxpayerController {
    private final TaxpayerConfig taxpayerConfig;

    public TaxpayerController(TaxpayerConfig taxpayerConfig) {
        this.taxpayerConfig = taxpayerConfig;
    }

    @GetMapping("/fill/{taxpayer}")
    public TaxpayerVO fill(
            @PathVariable String taxpayer
    ) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response1 = restTemplate.getForEntity(taxpayerConfig.getAddressAndDateURL() + "/" + taxpayer, String.class);
        ResponseEntity<String> response2 = restTemplate.getForEntity(taxpayerConfig.getFullNameAndStatusURL() + "/" + taxpayer, String.class);
        String address = null;
        LocalDate ld = null;
        String status = null;
        String fullName = null;
        boolean response1Success = response1.getStatusCode() == HttpStatus.OK && response1.hasBody();
        boolean response2Success = response2.getStatusCode() == HttpStatus.OK && response2.hasBody();
        if (response1Success || response2Success) {
            ObjectMapper om = new ObjectMapper();
            if (response1Success) {
                List<Map<String, Object>> objects1 = om.readValue(response1.getBody(), new TypeReference<>() {});
                try {
                    address = objects1.get(0).get("vadrprim").toString();
                } catch (Exception e) {}
                try {
                    ZonedDateTime zdt = ZonedDateTime.parse(objects1.get(0).get("dfrom").toString());
                    ld = LocalDate.ofInstant(zdt.toInstant(), ZoneOffset.UTC);
                } catch (Exception e) {}
            }
            if (response2Success) {
                List<Map<String, Object>> objects2 = om.readValue(response2.getBody(), new TypeReference<>() {});
                try {
                    Map preStatus = (Map) objects2.get(0).get("nsi00219");
                    status = preStatus.get("vnsostk").toString();
                } catch (Exception e) {}
                try {
                    fullName = objects2.get(0).get("vnaim").toString();
                } catch (Exception e) {}
            }
        }
        return TaxpayerVO.builder()
                .address(address)
                .registrationDate(ld)
                .status(status)
                .fullName(fullName)
                .build();
    }
}