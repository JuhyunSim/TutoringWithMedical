package com.simzoo.withmedical.enums;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String description;

    public static Gender fromDescription(String description) {
        return Arrays.stream(values())
            .filter(e -> e.description.equals(description))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_DATA_REQUEST));
    }
}
