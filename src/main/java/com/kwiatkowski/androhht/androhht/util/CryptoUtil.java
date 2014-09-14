package com.kwiatkowski.androhht.androhht.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 *
 * Original code by Nikolay Elenkov (2012)
 *
 * Elenkov, N. (2012a) Using Password-based Encryption on Android [Online]. Available at http://nelenkov.blogspot.co.uk/2012/04/using-password-based-encryption-on.html (Accessed 10 June 2014)
 * Elenkov, N. (2012b) Android password-based encryption (PBE) implementation [Source code]. Available at https://github.com/nelenkov/android-pbe (Accessed 10 June 2014)
 *
 *
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 *
 *
 *
 */
public class CryptoUtil {

    private static int ITERATION_COUNT = 1000;
    private static int KEY_LENGTH = 256;
    private static int SALT_LENGTH = KEY_LENGTH / 8;
    private static String DELIMITER = "]";
    private static SecureRandom random = new SecureRandom();


    public static byte[] generateIv(int length) {
        byte[] b = new byte[length];
        random.nextBytes(b);

        return b;
    }

    public static byte[] generateSalt() {
        byte[] b = new byte[SALT_LENGTH];
        random.nextBytes(b);

        return b;
    }


    public static String toHex(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }

        return buff.toString();
    }

    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }


    public static String encrypt(String plaintext, SecretKey key, byte[] salt) throws Exception{

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = generateIv(cipher.getBlockSize());

        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));


        if (salt != null) {
            return String.format("%s%s%s%s%s", toBase64(salt), DELIMITER,
                    toBase64(iv), DELIMITER, toBase64(cipherText));
        }

        return String.format("%s%s%s", toBase64(iv), DELIMITER,
                toBase64(cipherText));

    }

    public static SecretKey generateKey(byte[] salt, String password){
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            return key;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;

    }


    public static String decrypt(byte[] cipherBytes, SecretKey key, byte[] iv) throws Exception{

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);

            byte[] plaintext = cipher.doFinal(cipherBytes);
            String plainStr = new String(plaintext, "UTF-8");

            return plainStr;

    }

    public static String cryptoCode(String plaintext, String password) throws Exception{
            byte[] salt = generateSalt();
            SecretKey key = generateKey(salt, password);

            return encrypt(plaintext, key, salt);

    }

    public static String cryptoDecode(String ciphertext, String password) throws Exception{
        String[] fields = ciphertext.split(DELIMITER);
        if (fields.length != 3) {
            throw new IllegalArgumentException("Invalid encypted text format");
        }

        byte[] salt = fromBase64(fields[0]);
        byte[] iv = fromBase64(fields[1]);
        byte[] cipherBytes = fromBase64(fields[2]);
        SecretKey key = generateKey(salt, password);

        return decrypt(cipherBytes, key, iv);
    }

}
