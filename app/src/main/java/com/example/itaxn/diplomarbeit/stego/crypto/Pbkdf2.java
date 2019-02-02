package com.example.itaxn.diplomarbeit.stego.crypto;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Pbkdf2 {
    static final int saltLength = 16;
    static byte[] salt = new byte[saltLength];


    protected static byte[] generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        char[] chars = password.toCharArray();

        generateSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 32 * 8); // 256
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        System.out.println(hash.length);
        //for(int i=0; i<hash.length;i++) {System.out.println(hash[i]);}
        //return toHex(hash);
        //String s = new String(hash);
        //return s;
        return hash;
    }
    protected static byte[] generateStorngPasswordHash(String password, byte[] givenSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        char[] chars = password.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(chars, givenSalt, iterations, 32 * 8); // 256
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        //for(int i=0; i<hash.length;i++) {System.out.println(hash[i]);}
        //return toHex(hash);
        //String s = new String(hash);
        //return s;
        return hash;
    }

    protected static  void generateSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
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

    protected static byte[] getSalt(){
        return salt;
    }
}