package com.simzoo.withmedical.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentAmountInputDto {
    private Double total; // 총 금액
    private Double taxFree; // 비과세 금액 (Optional)
    private Double vat; // 부가세 금액 (Optional)
    private Double supply; // 과세 금액 (Optional)
}
