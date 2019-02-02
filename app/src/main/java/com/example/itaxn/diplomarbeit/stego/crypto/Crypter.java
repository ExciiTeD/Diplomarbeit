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
        System.out.println(skey.length);
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
            byte[] finalDataPackage = new byte[Pbkdf2.saltLength + iv.length + cipherMSG.length];
            System.out.println("mei soiz: " + new String(Pbkdf2.getSalt()));
            System.arraycopy(Pbkdf2.getSalt(), 0, finalDataPackage,0, Pbkdf2.saltLength);
            System.arraycopy(iv, 0, finalDataPackage, Pbkdf2.saltLength, iv.length);
            System.arraycopy(cipherMSG, 0, finalDataPackage, Pbkdf2.saltLength + iv.length , cipherMSG.length);
            return finalDataPackage;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public byte[] decode(String password, byte[] data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //Setting up the key
        byte[] salt = new byte[16];
        System.arraycopy(data,0, salt, 0, salt.length);
        System.out.println("mei soiz"+new String(salt));
        byte[] skey = this.hashPassword(password, salt);
        SecretKeySpec key = new SecretKeySpec(skey, "AES");

        // IV-Auslesen
        byte[] iv = new byte[ivLength];
        System.arraycopy(data, salt.length, iv, 0, iv.length);
        AlgorithmParameterSpec param = new IvParameterSpec(iv);
        //for (byte ivA : iv) System.out.print(ivA + "; ");

        //Restliche Daten auslesen
        byte[] encryptedMessage = new byte[data.length - (iv.length + salt.length)];
        System.arraycopy(data, (iv.length + salt.length), encryptedMessage, 0, encryptedMessage.length);
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

    private byte[] hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return Pbkdf2.generateStorngPasswordHash(password);
    }

    private byte[] hashPassword(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return Pbkdf2.generateStorngPasswordHash(password, salt);
    }

    public int getLastDataSize() {
        return this.lastDataSize;
    }
}

