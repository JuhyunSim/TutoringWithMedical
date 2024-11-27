package com.simzoo.withmedical.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeperatedAddressInputDto {

    private String base; //기본주소
    private String detail;
    private String postalCode;
    private String country; //국가코드

}
