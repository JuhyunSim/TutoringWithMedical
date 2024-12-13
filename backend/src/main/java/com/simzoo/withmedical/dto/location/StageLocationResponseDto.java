package com.simzoo.withmedical.dto.location;

import java.util.List;
import lombok.Getter;

@Getter
public class StageLocationResponseDto {
    private String id;          // 요청 ID
    private List<LocationData> result; // 결과 리스트
    private String errMsg;      // 에러 메시지
    private int errCd;          // 에러 코드
    private String trId;        // 트랜잭션 ID

    @Getter
    public static class LocationData {
        private String y_coor;      // Y좌표
        private String full_addr;   // 전체주소
        private String x_coor;      // X좌표
        private String addr_name;   // 시도명/시군구명/읍면동명
        private String cd;          // 시도/시군구/읍면동 코드
    }
}
