package com.simzoo.withmedical.util;

import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AesUtil {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    // 환경 변수에서 Secret Key 불러오기
    private static final String SECRET_KEY = System.getenv("MY_SECRET_KEY");

    public static String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(AesUtil.TRANSFORMATION);
            IvParameterSpec iv = generateIv();
            SecretKey key = new SecretKeySpec(Base64.getUrlDecoder().decode(SECRET_KEY.getBytes()), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            byte[] ivBytes = iv.getIV();

            return Base64.getEncoder().encodeToString(ivBytes) + ":" + Base64.getEncoder()
                .encodeToString(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | BadPaddingException |
                 IllegalBlockSizeException exception) {
            log.error(exception.getMessage(), exception);
            throw new CustomException(ErrorCode.ENCRYPTION_ERROR);
        }
    }

    public static String decrypt(String cipherText) {
        try {
            String[] parts = cipherText.split(":");
            byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
            byte[] cipherBytes = Base64.getDecoder().decode(parts[1]);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKey key = new SecretKeySpec(Base64.getUrlDecoder().decode(SECRET_KEY.getBytes()), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            log.info("cipher bytes: {}", Base64.getEncoder().encodeToString(cipherBytes));
            byte[] plainText = cipher.doFinal(cipherBytes);
            return new String(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | BadPaddingException |
                 IllegalBlockSizeException exception) {
            log.error(exception.getMessage());
            throw new CustomException(ErrorCode.DECRYPTION_ERROR);
        }
    }

    private static SecretKey generateKey(int n)
        throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static byte[] getSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
