package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.enums.sort.SortFields;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
@Builder
public class SortRequestDto<T extends Enum<T> & SortFields> {

    private T sortBy;
    private Sort.Direction direction;
}
