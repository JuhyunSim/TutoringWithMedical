package com.simzoo.withmedical.service;

import com.simzoo.withmedical.client.PortOneClient;
import com.simzoo.withmedical.dto.payment.BillingKeyPaymentRequestDto;
import com.simzoo.withmedical.dto.payment.PortOneTokenResponseDto;
import com.simzoo.withmedical.dto.payment.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PortOneClient portOneClient;

    public ScheduleResponseDto schedulePayment(String token, String paymentId,
        BillingKeyPaymentRequestDto requestDto) {
        return portOneClient.schedulePayment(token, paymentId, requestDto);
    }

    public PortOneTokenResponseDto getPortOneToken(String apiSecret) {
        return portOneClient.getApiToken(apiSecret);
    }
}
