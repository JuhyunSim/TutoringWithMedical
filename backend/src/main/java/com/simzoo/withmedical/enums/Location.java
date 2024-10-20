package com.simzoo.withmedical.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Location {
    SEOUL("서울"),
    INCHEON("인천"),
    BUSAN("부산");

    private final String description;
}
