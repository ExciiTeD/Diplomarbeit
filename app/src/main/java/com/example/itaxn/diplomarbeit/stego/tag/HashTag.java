package com.example.itaxn.diplomarbeit.stego.tag;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashTag extends Tag{
    private MessageDigest md;
    private String hexVal;

    public HashTag(String tag) {
        this.tag = tag;
        this.hexVal = tag.substring(1, 129);
    }

    public HashTag(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }

        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(data);

            this.hexVal = HashTag.toHex(md.digest());
            this.tag = "<" + this.hexVal + ">";
            this.tagLength = this.tag.length();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getHexVal() {
        return this.hexVal;
    }

    public static String toHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
