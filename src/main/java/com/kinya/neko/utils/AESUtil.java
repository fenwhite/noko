package com.kinya.neko.utils;

import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @author ：white
 * @description ：Advanced Encryption Standard tools
 * @date ：2022/4/13
 */

public class AESUtil {
    public static final int KEY_SIZE = 128;
    // the AES secret key
    public static final String SECRET_KEY = "";
    // param of SecretKeyFactory.getInstance
    public static final String KEYSPEC_INST = "PBKDF2WithHmacSHA256";
    public static final String CIPHER_INST = "AES/CBC/PKCS5PADDING";

    public static final String AES = "AES";

    /**
     * implement input string encryption
     * @param plainText
     * @param slat
     * @return cipher text
     */
    public static String encrypt(String plainText,String slat) {
        String ans = null;

        try {
            SecretKey secret = getSecretKey(plainText, slat);

            // configure a cipher instance
            Cipher cipher = Cipher.getInstance(CIPHER_INST);
            cipher.init(Cipher.ENCRYPT_MODE, secret, getIvSpec());

            // encrypt the input string
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            // TODO why need to use base64?
            ans = Base64.getEncoder().encodeToString(cipherText);
        } finally {
            return ans;
        }
    }

    public static String decrypt(String cipherText,String slat) {
        String ans = null;
        try{
            SecretKey secret = getSecretKey(cipherText, slat);

            // configure a cipher instance
            Cipher cipher = Cipher.getInstance(CIPHER_INST);
            cipher.init(Cipher.DECRYPT_MODE, secret, getIvSpec());
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
            ans = new String(plainText);
        }finally {
            return ans;
        }
    }

    /**
     *  IV is a pseudo-random value and
     *  has the same size as the block that is encrypted.
     * @return generate by SecureRandom
     */
    private static IvParameterSpec getIvSpec() {
        // Generating IV.
        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        // IV to be filled in with random bytes.
        random.nextBytes(IV);
        return new IvParameterSpec(IV);
    }

    /**
     * Create KeySpec object and assign with constructor
     *
     * @param text
     * @param slat
     * @return SecretKey
     * @throws Exception
     */
    private static SecretKey getSecretKey(String text,String slat) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYSPEC_INST);
        KeySpec spec = new PBEKeySpec(text.toCharArray(),
                slat.getBytes(), 65536, KEY_SIZE);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(),
                AES);
        return secret;
    }

    public static String getSlat() {
        return KeyGenerators.string().generateKey();
    }
}
