package com.kinya.neko.utils;

import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author ：white
 * @description ：Encryption tools
 * @date ：2022/4/13
 */

public class EncryptUtil {

    public static String encryptWithAES(String plainText,String slat) {
        return AES.encrypt(plainText, slat);
    }

    public static String decryptWithAES(String cipherText,String slat) {
        return AES.decrypt(cipherText, slat);
    }

    public static String encryptWithRSA(String plainText) {
        return RSA.encrypt(plainText);
    }

    public static String decryptWithRSA(String cipherText) {
        return RSA.decrypt(cipherText);
    }

    public static String getSlat() {
        return KeyGenerators.string().generateKey();
    }

    private static class AES {
        private static final byte[] INITIAL_IV = {4, 0, 1, 0, 2, 0, 0, 4, 0, 0, 1, 0, 2, 0, 0, 0};
        public static final int KEY_SIZE = 128;
        // the AES secret key
        public static final String KEYSTORE_FORMAT = "JCEKS";
        public static final String KEYSTORE_PATH = "aes-keystore.jck";
        public static final String KEYSTORE_PASS = "emptyo";
        public static final String ALIAS = "neko";
        public static final String KEY_PASS = "nekopara";
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
                Key secret = getSecretKey(plainText, slat);

                // configure a cipher instance
                Cipher cipher = Cipher.getInstance(CIPHER_INST);
                cipher.init(Cipher.ENCRYPT_MODE, secret, getIvSpec());

                // encrypt the input string
                byte[] cipherText = cipher.doFinal(plainText.getBytes());
                // why need to use base64?
                ans = Base64.getEncoder().encodeToString(cipherText);
            } finally {
                return ans;
            }
        }

        public static String decrypt(String cipherText,String slat) {
            String ans = null;
            try{
                Key secret = getSecretKey(cipherText, slat);

                // configure a cipher instance
                Cipher cipher = Cipher.getInstance(CIPHER_INST);
                cipher.init(Cipher.DECRYPT_MODE, secret, getIvSpec());
                byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
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
            return new IvParameterSpec(INITIAL_IV);
        }

        /**
         * get key from file
         *
         * @param text
         * @param slat
         * @return SecretKey
         * @throws Exception
         */
        private static Key getSecretKey(String text,String slat) throws Exception {
            Key secret = null;
            // try to read from file(keyStore)
            InputStream keyStoreStream = EncryptUtil.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);
            // password to access keystore
            keyStore.load(keyStoreStream, KEYSTORE_PASS.toCharArray());
            if(keyStore.containsAlias(ALIAS)) {
                // password to access key
                secret = keyStore.getKey(ALIAS, KEY_PASS.toCharArray());
            } else {
                SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYSPEC_INST);
                KeySpec spec = new PBEKeySpec(text.toCharArray(),
                        slat.getBytes(), 65536, KEY_SIZE);
                secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(),
                        AES);
            }

            return secret;
        }

    }

    private static class RSA {
        private static String publicPath = "";
        private static String privatePath = "";

        public static PrivateKey getPrivateKey() throws Exception {
            File f = new File(privatePath);
            String key = new String(Files.readAllBytes(f.toPath()), Charset.defaultCharset());
            String pem = key.replace("-----BEGIN PRIVATE  KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE  KEY-----", "");
            byte[] decode = Base64.getDecoder().decode(pem);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            return keyFactory.generatePrivate(keySpec);
        }

        public static PublicKey getPublicKey() throws Exception {
            File f = new File(publicPath);
            String key = new String(Files.readAllBytes(f.toPath()), Charset.defaultCharset());
            String pem = key.replace("-----BEGIN PUBLIC   KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC   KEY-----", "");
            byte[] decode = Base64.getDecoder().decode(pem);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
            return keyFactory.generatePublic(keySpec);
        }

        public static String encrypt(String plainText) {
            String ans = null;
            try {
                // read public key from file
                Key publicKey = getPublicKey();
                // do encrypt
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                byte[] data = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
                // encode with base64
                ans = Base64.getEncoder().encodeToString(data);
            } finally {
                return ans;
            }
        }

        public static String decrypt(String cipherText) {
            String ans = null;
            try {
                // decode base64
                byte[] data = Base64.getDecoder().decode(cipherText);
                // read private-key from file
                Key privateKey = getPrivateKey();
                // do decrypt
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] plainData = cipher.doFinal(data);
                // build String with charset
                ans = new String(plainData, StandardCharsets.UTF_8);
            }finally {
                return ans;
            }
        }
    }
}
