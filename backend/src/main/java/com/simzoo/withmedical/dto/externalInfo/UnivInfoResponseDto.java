package com.simzoo.withmedical.dto.externalInfo;

import java.util.List;
import lombok.Getter;

@Getter
public class UnivInfoResponseDto {
    private DataSearch dataSearch;

    @Getter
    public static class DataSearch {
        private List<UniversityDto> content;
    }

    @Getter
    public static class UniversityDto {
        private String campustName;
        private String collegeinfourl;
        private String schoolType;
        private String link;
        private String schoolGubun;
        private String estType;
        private String schoolName; // 학교명
        private String region; // 지역
        private String adres; // 주소
    }
}



