package com.example.itaxn.diplomarbeit.stego.tag;

public abstract class Tag implements ITag {
    /**
     * Contains the tag as a String that will be inserted
     * into a wav.
     */
    protected String tag;

    /**
     * The length of the tag. That means that the variable
     * contains the amount of characters of the
     * length tag String.
     */
    protected int tagLength;

    /**
     * The value that the tag contains (an integer number).
     */
    protected long value;

    protected Tag() {
    }

    public Tag(String tag) {
        if ((tag != null && !tag.matches(ITag.regex)) || tag == null) {
            throw new IllegalArgumentException("This is a wrong tag");
        }

        this.tag = tag;
        this.tagLength = tag.length();
        this.value = Long.parseLong(this.tag.substring(1, tagLength - 1));
    }

    public String getTag() {
        return this.tag;
    }

    public long getTagVal() {
        return this.value;
    }

    public int getTagLen() {
        return this.tagLength;
    }

}
