package com.sviryd.taxpayer.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class TaxpayerVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String address;
    private LocalDate registrationDate;
    private String status;

}
