package com.simzoo.withmedical.client;

import com.simzoo.withmedical.dto.payment.BillingKeyPaymentRequestDto;
import com.simzoo.withmedical.dto.payment.PortOneTokenResponseDto;
import com.simzoo.withmedical.dto.payment.ScheduleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "portOneClient", url = "https://api.portone.io")
public interface PortOneClient {

    @PostMapping("/payments/{paymentId}/schedule")
    ScheduleResponseDto schedulePayment(
        @RequestHeader("Authorization") String token,
        @PathVariable("paymentId") String paymentId,
        @RequestBody BillingKeyPaymentRequestDto requestDto);

    @PostMapping("/login/api-secret")
    PortOneTokenResponseDto getApiToken(@RequestBody String apiSecret);

}
