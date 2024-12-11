package com.simzoo.withmedical.enums;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Location {
    SEOUL("서울"),
    INCHEON("인천"),
    BUSAN("부산");

    private final String description;

    public static Location fromDescription(String description) {
        return Arrays.stream(values())
            .filter(e -> e.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_DATA_REQUEST));
    }
}
