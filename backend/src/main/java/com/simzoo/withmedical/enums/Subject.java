package com.simzoo.withmedical.enums;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Subject {
    ELEMENTARY_MATH("초등수학"),
    MIDDLE_MATH("중등수학"),
    HIGH_MATH("고등수학"),
    ELEMENTARY_ENGLISH("초등영어"),
    MIDDLE_ENGLISH("중등영어"),
    HIGH_ENGLISH("고등영어");

    private final String description;

    public static Subject fromDescription(String description) {
        return Arrays.stream(values())
            .filter(e -> e.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_DATA_REQUEST));
    }
}
