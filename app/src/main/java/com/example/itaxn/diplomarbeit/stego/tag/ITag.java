package com.example.itaxn.diplomarbeit.stego.tag;

public interface ITag {
    /**
     * Contains the regular expression for the checksum tag.
     * You can us it to test if a String matches the structure
     * of the checksum tag.
     */
    public static final String regex = "^<\\d+>$";

    /**
     * @return the tag as a String.
     */
    public String getTag();

    /**
     * @return the value of the tag
     */
    public long getTagVal();

    /**
     * @return the length of the tag string (amount of used characters).
     */
    public int getTagLen();
}
