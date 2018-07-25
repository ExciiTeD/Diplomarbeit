package com.example.itaxn.diplomarbeit.stego.tag;

public class CheckLengthTag extends Tag {
    /**
     * Creates the tag based on the parameter.
     *
     * @param tag the tag as string.
     */
    public CheckLengthTag(String tag) {
        super(tag);
    }

    /**
     * Creates a tag based on the parameter
     *
     * @param value
     */
    public CheckLengthTag(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }

        this.value = value;
        this.tag = "<" + this.value + ">";
        this.tagLength = this.tag.length();
    }
}
