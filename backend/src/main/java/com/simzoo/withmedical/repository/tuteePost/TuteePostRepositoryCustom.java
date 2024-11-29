package com.simzoo.withmedical.repository.tuteePost;

import com.simzoo.withmedical.dto.SortRequestDto;
import com.simzoo.withmedical.dto.filter.FilterRequestDto;
import com.simzoo.withmedical.dto.tuteePost.TuteePostingSimpleResponseDto;
import com.simzoo.withmedical.enums.sort.TuteePostSortCriteria;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TuteePostRepositoryCustom {

    Page<TuteePostingSimpleResponseDto> findAllTuteePostings(Pageable pageable,
        List<SortRequestDto<TuteePostSortCriteria>> sortRequests,
        FilterRequestDto filterRequest);
}
