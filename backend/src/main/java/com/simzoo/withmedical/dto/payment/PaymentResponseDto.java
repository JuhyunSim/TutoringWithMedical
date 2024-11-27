package com.simzoo.withmedical.dto.payment;

import com.simzoo.withmedical.enums.payment.PayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDto {

    private PayStatus status;
    private String id;
    private String transactionId;
    private String merchantId;
    private String storeId;
    private String requestedAt;
    private String updatedAt;
    private String statusChangedAt;
    private String orderName;
    private PaymentAmount paymentAmount;
    private String currency;

    static class PaymentAmount {
        Integer total;
        Integer taxFree;
        Integer vat;
        Integer supply;
        Integer discount;
        Integer paid;
        Integer cancelled;
        Integer cancelledTaxFree;
    }

}
