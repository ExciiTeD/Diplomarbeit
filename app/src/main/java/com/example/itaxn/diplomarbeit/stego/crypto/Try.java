package com.example.itaxn.diplomarbeit.stego.crypto;

public class Try {
    public static void main(String args[]) throws Exception {


        String text = "ErfolgreichErfolgreichaaaaaaaaaaaa";
        String pw = "schule123schule123schule123";
        String pw2 = "LULULUsdgfstew";

        Crypter c = new Crypter();

        byte[] enc = c.encode(pw, text.getBytes());
//        System.out.println(new String(enc));
        // System.out.println("EncLength: " + enc.length);
        byte[] iv = new byte[16];
        System.arraycopy(enc, 0, iv, 0, iv.length);
        byte[] cipherText = new byte[enc.length - iv.length];
//        System.arraycopy(enc, iv.length, cipherText, 0, cipherText.length);
//        System.out.println(new String(cipherText).length());

        String dencMSG = new String(c.decode(pw, enc));
        System.out.println(new String(c.decode(pw, cipherText, 0, 16, iv)));
        System.out.println("Enc MSG: " + dencMSG);


    }
}
