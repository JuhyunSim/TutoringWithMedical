package com.simzoo.withmedical.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponseDto {

    private PaymentScheduleSummary schedule;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class PaymentScheduleSummary {
        private String id; // 결제 예약 건 아이디
        private String paymentId; // 결제 건 아이디
        private String status; // 상태
        private String timeToPay; // 결제 예정 시점
    }
}
