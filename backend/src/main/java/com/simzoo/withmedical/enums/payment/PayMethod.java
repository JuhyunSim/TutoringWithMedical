package com.simzoo.withmedical.enums.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayMethod {
    CARD ("카드"),
    VIRTUAL_ACCOUNT ("가상계좌"),
    TRANSFER ("계좌이체"),
    MOBILE ("휴대폰 소액결제"),
    GIFT_CERTIFICATE ("상품권"),
    EASY_PAY ("간편결제");

    private final String name;
}
