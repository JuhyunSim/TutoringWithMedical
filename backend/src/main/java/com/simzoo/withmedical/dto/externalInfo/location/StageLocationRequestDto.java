package com.simzoo.withmedical.dto.externalInfo.location;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StageLocationRequestDto {
    @Size(max = 5, message = "cd는 최대 5자까지만 입력 가능합니다.")
    private String cd;

    @Builder.Default
    @Pattern(regexp = "0|1", message = "pg_yn은 0 또는 1만 입력 가능합니다.")
    private String pg_yn = "0";

    public void setCdNull() {
        this.cd = null;
    }
}
