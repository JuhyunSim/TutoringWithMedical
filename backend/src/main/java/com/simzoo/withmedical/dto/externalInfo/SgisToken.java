package com.simzoo.withmedical.dto.externalInfo;

import lombok.Getter;

@Getter
public class SgisToken {
    Result result;

    @Getter
    public static class Result {
        String accessToken;
        String accessTimout;
    }
}
