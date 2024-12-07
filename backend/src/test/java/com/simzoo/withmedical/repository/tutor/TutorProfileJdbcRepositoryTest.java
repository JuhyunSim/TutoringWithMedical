package com.simzoo.withmedical.repository.tutor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TutorProfileJdbcRepositoryTest {

    @Autowired
    private TutorProfileJdbcRepository tutorProfileJdbcRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findFilteredProfilesTest() {
        //given



        //when



        //then

    }

}