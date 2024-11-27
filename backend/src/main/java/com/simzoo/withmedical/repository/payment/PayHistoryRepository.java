package com.simzoo.withmedical.repository.payment;

import com.simzoo.withmedical.entity.payment.PayHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayHistoryRepository extends JpaRepository<PayHistoryEntity, Long> {

}
