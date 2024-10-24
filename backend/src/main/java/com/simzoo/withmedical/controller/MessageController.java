package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.service.MemberService;
import com.simzoo.withmedical.service.VerificationService;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MessageController {

    final DefaultMessageService messageService;
    private final VerificationService verificationService;

    @Value("${coolsms.sender_phone}")
    String senderPhone;

    public MessageController(@Value("${coolsms.secret}") String SECRET_KEY,
        @Value("${coolsms.api_key}")
        String API_KEY, VerificationService verificationService, MemberService memberService) {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, SECRET_KEY,
            "https://api.coolsms.co.kr");
        this.verificationService = verificationService;
    }

    /**
     * 단일 메시지 발송 예제
     */
    @PostMapping("/send-one")
    public SingleMessageSentResponse sendOne(@RequestParam String receivePhone) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        Integer verificationNum = getVerifyNumber();
        message.setFrom(senderPhone);
        message.setTo(receivePhone);
        message.setText("본인확인을 위한 인증번호 " + verificationNum + "입력해주세요.");
        verificationService.saveVerificationNumber(receivePhone, verificationNum);

        SingleMessageSentResponse response = this.messageService.sendOne(
            new SingleMessageSendingRequest(message));
        log.info(String.valueOf(response));

        return response;
    }

    private Integer getVerifyNumber() {
        return 10000000 + new Random().nextInt(90000000);
    }
}