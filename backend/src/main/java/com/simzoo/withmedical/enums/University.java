package com.simzoo.withmedical.enums;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum University {
    SEOUL_UNIVERSITY("서울대학교"),
    YONSEI_UNIVERSITY("연세대학교"),
    KOREA_UNIVERSITY("고려대학교"),
    SUNKYUNKWAN_UNIVERSITY("성균관대학교");

    private final String description;

    public static University fromDescription(java.lang.String description) {
        return Arrays.stream(values())
            .filter(e -> e.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_DATA_REQUEST));
    }
}
