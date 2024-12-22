package com.sviryd.taxpayer.controller.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sviryd.taxpayer.config.TaxpayerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/taxpayer")
public class TaxpayerController {
    private final TaxpayerConfig egrConfig;

    public TaxpayerController(TaxpayerConfig egrConfig) {
        this.egrConfig = egrConfig;
    }

    @GetMapping("/fill/{unp}")
    public String fill(
            @PathVariable String unp
    ) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> r = null;
        try {
            r = restTemplate.getForEntity(egrConfig.getFullNameAndStatusURL() + "/" + unp, String.class);
        } catch (Exception e) {
        }
        String fullName = null;
        boolean r2Success = r != null && r.getStatusCode() == HttpStatus.OK && r.hasBody();
        if (r2Success) {
            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode root = om.readTree(r.getBody());
                root = root.get(0);
                fullName = root.path("vnaim").asText();
            } catch (Exception e) {
            }
        }
        return fullName;
    }
}