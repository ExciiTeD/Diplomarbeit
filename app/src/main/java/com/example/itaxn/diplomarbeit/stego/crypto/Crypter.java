package com.example.itaxn.diplomarbeit.stego.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

public class Crypter {
    static final String cipher_type = "AES/CBC/NoPadding";
    static int keySize = 16;
    static int ivLength = 16;
    private int lastDataSize;


    public byte[] encode(String password, byte[] data)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        //Generating Random IV
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[ivLength];
        secureRandom.nextBytes(iv);
        //for (byte ivA : iv) System.out.print(ivA + "; ");

        //Setting up the Key
        byte[] skey = this.hashPassword(password);
        SecretKeySpec key = new SecretKeySpec(skey, "AES");

        //Padding the Data
        int rest = data.length % 16;
        int length;
        if (rest != 0) {
            length = data.length + (16 - rest);
        } else {
            length = data.length;
        }
        byte[] paddedArray = new byte[length];
        this.lastDataSize = paddedArray.length;

        System.arraycopy(data, 0, paddedArray, 0, data.length);
        String paddedData = new String(paddedArray);


        AlgorithmParameterSpec param = new IvParameterSpec(iv);

        try {
            //Setting up the Cipher
            Cipher cipher = Cipher.getInstance(cipher_type);
            cipher.init(Cipher.ENCRYPT_MODE, key, param);
            //Encrypt the Message
            byte[] cipherMSG = cipher.doFinal(paddedData.getBytes());
            //Setting up the whole Data package, with IV and encrypted Text
            byte[] finalDataPackage = new byte[iv.length + cipherMSG.length];
            System.arraycopy(iv, 0, finalDataPackage, 0, iv.length);
            System.arraycopy(cipherMSG, 0, finalDataPackage, iv.length, cipherMSG.length);
            return finalDataPackage;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public byte[] decode(String password, byte[] data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //Setting up the key
        byte[] skey = this.hashPassword(password);
        SecretKeySpec key = new SecretKeySpec(skey, "AES");

        // IV-Auslesen
        byte[] iv = new byte[ivLength];
        System.arraycopy(data, 0, iv, 0, iv.length);
        AlgorithmParameterSpec param = new IvParameterSpec(iv);
        //for (byte ivA : iv) System.out.print(ivA + "; ");

        //Restliche Daten auslesen
        byte[] encryptedMessage = new byte[data.length - iv.length];
        System.arraycopy(data, iv.length, encryptedMessage, 0, encryptedMessage.length);
        //for(byte eM : encryptedMessage) System.out.print(eM + "; ");


        try {
            //Setting up the Cipher
            Cipher cipher = Cipher.getInstance(cipher_type);
            cipher.init(Cipher.DECRYPT_MODE, key, param);
            //Encrypt the Message
            byte[] decodedMessage = cipher.doFinal(encryptedMessage);
            String trimmedMSG = new String(decodedMessage).replaceAll("\0", "");
            return trimmedMSG.getBytes();
            //return decodedMessage;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public byte[] decode(String password, byte[] data, int offset, int length, byte[] iv) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        //Setting up the key
        byte[] skey = this.hashPassword(password);
        SecretKeySpec key = new SecretKeySpec(skey, "AES");

        // IV
        AlgorithmParameterSpec param = new IvParameterSpec(iv);


        //Restliche Daten auslesen
        byte[] encryptedMessage = new byte[length];
        System.arraycopy(data, offset, encryptedMessage, 0, encryptedMessage.length);
        //System.out.println("eM: ");
        for (byte eM : encryptedMessage) System.out.print(eM + "; ");

        Cipher cipher = Cipher.getInstance(cipher_type);
        cipher.init(Cipher.DECRYPT_MODE, key, param);
        //Encrypt the Message
        byte[] decodedMessage = cipher.doFinal(encryptedMessage);
        String trimmedMSG = new String(decodedMessage).replaceAll("\0", "");
        return trimmedMSG.getBytes();
    }

    private byte[] hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] pwB = Pbkdf2.generateStorngPasswordHash(password).getBytes();
        byte[] skey = new byte[keySize];
        System.arraycopy(pwB, 0, skey, 0, skey.length);
        return skey;
    }

    public int getLastDataSize() {
        return this.lastDataSize;
    }
}

