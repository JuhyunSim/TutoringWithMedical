package com.simzoo.withmedical.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationDto {
    private Sido sido;
    private Sigungu sigungu;

    @Getter
    @Builder
    public static class Sido {
        private String cd;
        private String addr_name;
        private String full_addr;
    }

    @Getter
    @Builder
    public static class Sigungu {
        private String cd;
        private String addr_name;
        private String full_addr;
    }

}


