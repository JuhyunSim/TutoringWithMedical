package com.simzoo.withmedical.dto.school;

import java.util.List;
import lombok.Getter;

@Getter
public class SchoolInfoResponseDto {
    private DataSearch dataSearch;

    @Getter
    public static class DataSearch {
        private List<SchoolDto> content;
    }

    @Getter
    public static class SchoolDto {
        private String campusName;
        private String collegeinfourl;
        private String schoolType;
        private String link;
        private String schoolGubun;
        private String estType;
        private String schoolName; // 학교명
        private String region; // 지역
        private String adres; // 주소
        private String seq;
    }
}



