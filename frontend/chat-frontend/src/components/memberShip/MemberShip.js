import React from "react";
import * as PortOne from "@portone/browser-sdk/v2";

const Membership = () => {
    const requestPayment = async () => {
        try {
            // 빌링키 발급 요청
            const issueResponse = await PortOne.requestIssueBillingKey({
                storeId: "store-2ca052c3-9005-4e57-928f-05a4ce6a0d4a", // Store ID
                channelKey: "channel-key-1d959a01-9eba-4451-baaa-de922001b506", // Channel Key
                billingKeyMethod: "CARD", // 결제 방법
                merchantId: "toss-payments", // 상점 ID
                issueName: "토스페이먼츠 테스트", // 요청 이름
            });

            // 발급 실패 처리
            if (issueResponse.code !== undefined) {
                return alert(`빌링키 발급 오류: ${issueResponse.message}`);
            }

            // 서버에 빌링키 전달
            const response = await fetch(`${process.env.REACT_APP_BACKEND_URL}/billings`, {
                method: "POST",
                headers: { "Content-Type": "application/json" }, // 'headers'로 수정
                body: JSON.stringify({
                    billingKey: issueResponse.billingKey, // 빌링키 전달
                    // 추가 요청 데이터
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(`서버 오류: ${JSON.stringify(errorData)}`);
            }

            alert("결제가 성공적으로 완료되었습니다!");
        } catch (error) {
            console.error("결제 처리 중 오류 발생:", error);
            alert(`결제 오류: ${error.message}`);
        }
    };

    return (
        <div style={{ padding: "20px", textAlign: "center" }}>
            <h1>멤버십 가입</h1>
            <p>멤버십을 통해 다양한 혜택을 누려보세요!</p>
            <button className="payment-button" onClick={requestPayment}>
                결제하기
            </button>
        </div>
    );
};

export default Membership;
