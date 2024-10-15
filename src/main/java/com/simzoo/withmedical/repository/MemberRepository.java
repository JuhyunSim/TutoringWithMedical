package com.simzoo.withmedical.repository;

import com.simzoo.withmedical.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

}
