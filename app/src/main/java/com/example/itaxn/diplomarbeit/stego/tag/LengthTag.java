package com.example.itaxn.diplomarbeit.stego.tag;

/**
 * This class describes the Tag that is saved at the
 * beginning of a manipulated wav file. You can get access to
 * the tag itself and its size. The Syntax for such a Tag is
 * <integer Number>.
 * <p>
 * For instance: <23520>
 *
 * @author Poehli.a
 */
public class LengthTag extends Tag {
    /**
     * Creates the tag based on the parameter.
     *
     * @param tag the tag as string.
     */
    public LengthTag(String tag) {
        super(tag);
    }

    public LengthTag(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }

        this.value = value;
        this.tag = "<" + this.value + ">";
        this.tagLength = this.tag.length();
    }
}
