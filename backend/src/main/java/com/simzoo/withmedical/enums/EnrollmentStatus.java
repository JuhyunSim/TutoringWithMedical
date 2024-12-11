package com.simzoo.withmedical.enums;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnrollmentStatus {
    ENROLLED("재학"),
    LEAVE_OF_ABSENCE("휴학"),
    GRADUATED("졸업"),
    DROPPED_OUT("중퇴");

    private final String description;

    public static EnrollmentStatus fromDescription(String description) {
        return Arrays.stream(values())
            .filter(status -> status.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_DATA_REQUEST));
    }
}
