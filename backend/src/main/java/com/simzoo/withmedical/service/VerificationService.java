package com.simzoo.withmedical.service;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final RedisTemplate<String, Integer> redisTemplate;

    @Transactional
    public void saveVerificationNumber(String phoneNumber, Integer verificationNumber) {
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        valueOps.set(phoneNumber, verificationNumber, 3, TimeUnit.MINUTES);
    }

    @Transactional
    public void verifyNumber(String phoneNumber, Integer verificationNumber) {
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        Integer savedNumber = valueOps.get(phoneNumber);

        if (savedNumber == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_VERIFY_NUMBER);
        }

        if (!verificationNumber.equals(savedNumber)) {
            throw new CustomException(ErrorCode.VERIFY_NUMBER_NOT_MATCH);
        }
    }
}
