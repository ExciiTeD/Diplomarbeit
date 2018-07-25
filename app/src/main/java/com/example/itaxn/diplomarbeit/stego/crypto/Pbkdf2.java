package com.example.itaxn.diplomarbeit.stego.crypto;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Pbkdf2 {

    protected static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 32 * 8); // 256
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        //for(int i=0; i<hash.length;i++) {System.out.println(hash[i]);}
        return toHex(hash);
        //String s = new String(hash);
        //return s;

    }

    protected static byte[] getSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        for (int i = 0; i < salt.length; i++) {
            salt[i] = 1;
        }
        return salt;
    }

    protected static String toHex(byte[] array) throws NoSuchAlgorithmException {

        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

}