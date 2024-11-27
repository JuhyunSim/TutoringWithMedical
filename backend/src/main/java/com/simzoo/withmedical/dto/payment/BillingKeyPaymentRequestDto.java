package com.simzoo.withmedical.dto.payment;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingKeyPaymentRequestDto {
    private String storeId; // 상점 아이디 (Optional)
    private String billingKey; // 빌링키
    private String channelKey; // 채널 키 (Optional)
    private String orderName; // 주문명
    private CustomerInputDto customer; // 고객 정보 (Optional)
    private String customData; // 사용자 지정 데이터 (Optional)
    private PaymentAmountInputDto amount; // 금액 세부 정보
    private String currency; // 통화 단위
    private Integer installmentMonth; // 할부 개월 수 (Optional)
    private Boolean useFreeInterestFromMerchant; // 무이자 할부 여부 (Optional)
    private Boolean useCardPoint; // 카드 포인트 사용 여부 (Optional)
    private CashReceiptInputDto cashReceipt; // 현금영수증 정보 (Optional)
    private String country; // 국가 (Optional)
    private List<String> noticeUrls; // 웹훅 주소 (Optional)
    private List<PaymentProductDto> products; // 상품 정보 (Optional)
    private Integer productCount; // 상품 개수 (Optional)
    private String productType; // 상품 유형 (Optional)
    private SeperatedAddressInputDto shippingAddress; // 주소 정보 (Optional)
    private String promotionId; // 프로모션 ID (Optional)
    private Object bypass; // PG사별 추가 파라미터 (Optional)
    private String timeToPay; // 결제 예정 시점 (RFC 3339 date-time)
}
