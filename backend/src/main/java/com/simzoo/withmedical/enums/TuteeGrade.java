package com.simzoo.withmedical.enums;

import static com.simzoo.withmedical.enums.GradeType.ELEMENTARY;
import static com.simzoo.withmedical.enums.GradeType.HIGH;
import static com.simzoo.withmedical.enums.GradeType.MIDDLE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TuteeGrade {
    ELEMENTARY_1("초1", 1, ELEMENTARY),
    ELEMENTARY_2("초2", 2, ELEMENTARY),
    ELEMENTARY_3("초3", 3, ELEMENTARY),
    ELEMENTARY_4("초4", 4, ELEMENTARY),
    ELEMENTARY_5("초5", 5, ELEMENTARY),
    ELEMENTARY_6("초6", 6, ELEMENTARY),
    MIDDLE_1("중1", 7, MIDDLE),
    MIDDLE_2("중2", 8, MIDDLE),
    MIDDLE_3("중3", 9, MIDDLE),
    HIGH_1("고1", 10, HIGH),
    HIGH_2("고2", 11, HIGH),
    HIGH_3("고3", 12, HIGH),;

    private final String description;
    private final int order;
    private final GradeType group;

}
