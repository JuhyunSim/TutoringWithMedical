package com.simzoo.withmedical.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnrollmentStatus {
    ENROLLLED("재학"),
    LEAVE_OF_ABSENCE("휴학"),
    GRADUATED("졸업"),
    DROPPED_OUT("중퇴");

    private final String description;
}
