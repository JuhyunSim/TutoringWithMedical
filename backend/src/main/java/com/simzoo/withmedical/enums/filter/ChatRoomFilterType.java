package com.simzoo.withmedical.enums.filter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChatRoomFilterType {
    ALL("전체"),
    CREATED_BY_ME("요청한 대화"),
    INVITED("요청받은 대화");
    private String description;

}
