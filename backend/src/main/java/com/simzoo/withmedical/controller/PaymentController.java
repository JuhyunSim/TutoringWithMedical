package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.payment.BillingKeyPaymentRequestDto;
import com.simzoo.withmedical.dto.payment.PortOneTokenResponseDto;
import com.simzoo.withmedical.dto.payment.ScheduleResponseDto;
import com.simzoo.withmedical.service.PaymentService;
import com.simzoo.withmedical.util.resolver.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 예약(portone)
     */
    @PostMapping("/{paymentId}/schedule")
    public ResponseEntity<ScheduleResponseDto> preRegister(@LoginId Long userId,
        @RequestHeader("Authorization") String token,
        @PathVariable String paymentId, @RequestBody BillingKeyPaymentRequestDto requestDto) {

        return ResponseEntity.ok(paymentService.schedulePayment(token, paymentId, requestDto));
    }

    /**
     * portone 토큰 발급
     */
    @PostMapping("/login/api-secret")
    public ResponseEntity<PortOneTokenResponseDto> getToken(@RequestBody String apiSecret) {
        return ResponseEntity.ok(paymentService.getPortOneToken(apiSecret));
    }

}

