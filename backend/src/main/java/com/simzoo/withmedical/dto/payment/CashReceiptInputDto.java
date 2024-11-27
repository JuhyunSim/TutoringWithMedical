package com.simzoo.withmedical.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashReceiptInputDto {
    private String type; // 현금영수증 유형
    private String receiptNumber; //현금영수증 번호
}
