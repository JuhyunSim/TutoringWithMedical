import React, { useEffect } from "react";
import * as PortOne from "@portone/browser-sdk/v2";

const Membership = () => {

    const requestIssueBillingKey = ()=>  {
        PortOne.requestIssueBillingKey({
            storeId: "store-2ca052c3-9005-4e57-928f-05a4ce6a0d4a", // 고객사 storeId
            channelKey: "channel-key-7fcb2d36-f388-4fd9-a049-664f3db277c4", // 채널 키
            billingKeyMethod: "CARD", // 결제 방식
            issueId: "test-issueId",
            issueName: "멤버십 결제 빌링키", // 발급 이름
            customer: {
                fullName: "포트원", // 고객 이름
                phoneNumber: "010-0000-1234", // 고객 전화번호
                email: "test@portone.io", // 고객 이메일
            },
        })
            .then((response) => {
                // 성공 처리
                console.log("빌링키 발급 성공:", response);
                alert("빌링키 발급이 완료되었습니다.");
            })
            .catch((error) => {
                // 실패 처리
                console.error("빌링키 발급 실패:", error);
                alert("빌링키 발급에 실패했습니다.");
            });
      }

    return (
        <div style={{ padding: "20px", textAlign: "center" }}>
            <h1>멤버십 가입</h1>
            <p>멤버십을 통해 다양한 혜택을 누려보세요!</p>
            <button className="payment-button" onClick={requestIssueBillingKey}>
                결제하기
            </button>
        </div>
    );
};

export default Membership;