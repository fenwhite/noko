package com.kinya.neko.utils;

import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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

    public static Key getAESecretKey(String alias, String pass) {
        return AES.getSecretKey(alias, pass);
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
        // algorithm/mode/padding
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
                Key secret = getSecretKey(ALIAS, KEY_PASS);

                // configure a cipher instance
                Cipher cipher = Cipher.getInstance(CIPHER_INST);
                cipher.init(Cipher.ENCRYPT_MODE, secret, getIvSpec());

                // encrypt the input string
                plainText = plainText + slat;
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
                Key secret = getSecretKey(ALIAS, KEY_PASS);

                // configure a cipher instance
                Cipher cipher = Cipher.getInstance(CIPHER_INST);
                cipher.init(Cipher.DECRYPT_MODE, secret, getIvSpec());
                byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
                String salText = new String(plainText);
                ans = salText.substring(0, salText.length() - slat.length());
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
         * @param alias key alias
         * @param pass password of the alias
         * @return SecretKey
         * @throws Exception maybe password is worry
         */
        private static Key getSecretKey(String alias, String pass) {
            Key secret = null;
            InputStream keyStoreStream = EncryptUtil.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            KeyStore keyStore = null;
            try {
                keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);

                // password to access keystore
                keyStore.load(keyStoreStream, KEYSTORE_PASS.toCharArray());
                if(keyStore.containsAlias(alias)) {
                    // password to access key
                    secret = keyStore.getKey(alias, pass.toCharArray());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return secret;
        }
    }

    private static class RSA {
        private static String INST = "RSA/ECB/PKCS1Padding";

        private static String privatePath = "pkcs8.key";
        private static String publicPath = "publickey.crt";

        public static PrivateKey getPrivateKey() throws Exception {
            URL privateKeyRes = EncryptUtil.class.getClassLoader().getResource(privatePath);
            File privateFile = new File(privateKeyRes.getFile());

            String key = new String(Files.readAllBytes(privateFile.toPath()), Charset.defaultCharset());
            String pem = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("\n", "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] decode = Base64.getDecoder().decode(pem);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            return keyFactory.generatePrivate(keySpec);
        }

        public static PublicKey getPublicKey() throws Exception {
            URL publicKeyUrl = EncryptUtil.class.getClassLoader().getResource(publicPath);
            File publicFile = new File(publicKeyUrl.getFile());
            String key = new String(Files.readAllBytes(publicFile.toPath()), Charset.defaultCharset());
            String pem = key.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("\n", "")
                    .replace("-----END PUBLIC KEY-----", "");
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
                Cipher cipher = Cipher.getInstance(INST);
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
