package com.simzoo.withmedical.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AesUtilTest {

    @Test
    void aesTest() {
        //given
        String plainText = "Hello World!";
        String encryptedText = AesUtil.encrypt(plainText);
        String decryptedText = AesUtil.decrypt(encryptedText);


        //when
        //then
        assertEquals(plainText, decryptedText);

    }
}