package com.simzoo.withmedical.repository.tutor;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.config.QueryDslConfig;
import com.simzoo.withmedical.dto.TutorSimpleResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 임베디드 DB 비활성화
@Import(QueryDslConfig.class)
@EntityScan(basePackages = {"com.simzoo.withmedical.entity"})
class TutorProfileRepositoryCustomImplTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Test
    void testFindAllTutorProfiles() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "createdAt");

        //when
        Page<TutorSimpleResponseDto> tutorProfileDtos = tutorProfileRepository.findTutorProfileDtos(
            pageable);

        //then
    }
}