package com.simzoo.withmedical.util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGenerate {

    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, new SecureRandom());
        SecretKey key = keyGen.generateKey();

        String base64key = Base64.getUrlEncoder().withoutPadding().encodeToString(key.getEncoded());
        System.out.println("key encoded: " + base64key);
    }

}
