package com.simzoo.withmedical.enums.sort;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TuteePostSortCriteria implements SortFields {
    CREATED_AT("createdAt"),
    FEE("fee"),
    TUTEE_GRADE("grade.order");

    private final String field;
}
