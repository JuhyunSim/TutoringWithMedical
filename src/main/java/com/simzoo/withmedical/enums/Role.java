package com.simzoo.withmedical.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    TUTEE("학생"),
    TUTOR("선생님"),
    PARENT("학부모");

    private String description;
}
