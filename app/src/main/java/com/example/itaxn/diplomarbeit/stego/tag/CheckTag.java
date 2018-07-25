package com.example.itaxn.diplomarbeit.stego.tag;

import java.util.zip.CRC32;

public class CheckTag extends Tag {
    private CRC32 crc;

    public CheckTag(String tag) {
        super(tag);
    }

    /**
     * Sets the check tag by the given data byte Array.
     * It will be a CRC32 Checksum created through the
     * data array and inserted into the tag.
     *
     * @param data
     */
    public CheckTag(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }

        crc = new CRC32();
        crc.update(data);

        this.value = crc.getValue();
        this.tag = "<" + this.value + ">";
        this.tagLength = this.tag.length();

        if (!this.tag.matches(ITag.regex)) {
            throw new IllegalArgumentException();
        }
    }

}
