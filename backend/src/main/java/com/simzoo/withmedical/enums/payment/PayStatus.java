package com.simzoo.withmedical.enums.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayStatus {
    CANCELLED("결제 취소"),
    FAILED("결제 실패"),
    PAID("결제 완료"),
    PARTIAL_CANCELLED("결제 부분 취소"),
    PAY_PENDING("결제 완료 대기"),
    READY("결제 준비"),
    VIRTUAL_ACCOUNT_ISSUED("가상계좌 발급 완료");

    private String description;
}
