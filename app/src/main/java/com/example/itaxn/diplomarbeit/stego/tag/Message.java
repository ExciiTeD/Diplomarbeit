package com.example.itaxn.diplomarbeit.stego.tag;

public class Message {
    /**
     * Contains the message that should be hidden.
     */
    private byte[] message;

    /**
     * the length tag of the message
     */
    private LengthTag lTag;

    /**
     * the check length tag of the message.
     */
    private CheckLengthTag clTag;

    /**
     * The check tag of the message.
     */
    private CheckTag cTag;

    public Message() {
    }

    /**
     * Sets the message and tags by the given message.
     * And then generates the length checktTag length
     * and checksum of the message.
     *
     * @param message
     */
    public Message(byte[] message) {
        if (message == null) {
            throw new NullPointerException("At message in Message object");
        }
        this.setMessage(message);
        this.generateTags();
    }

    /**
     * Generates the tags through the message.
     */
    private void generateTags() {
        this.cTag = new CheckTag(this.message);
        this.clTag = new CheckLengthTag(cTag.getTagLen());
        this.lTag = new LengthTag(this.message.length);
    }

    /**
     * @param message, not null
     */
    public void setMessage(byte[] message) {
        if (message == null)
            throw new IllegalArgumentException();
        this.message = message;
    }

    /**
     * @param lTag, not null
     */
    public void setlTag(LengthTag lTag) {
        if (lTag == null)
            throw new IllegalArgumentException();
        this.lTag = lTag;
    }

    /**
     * @param clTag, not null
     */
    public void setCLTag(CheckLengthTag clTag) {
        if (clTag == null)
            throw new IllegalArgumentException();
        this.clTag = clTag;
    }

    /**
     * @param cTag, not null
     */
    public void setcTag(CheckTag cTag) {
        if (cTag == null)
            throw new IllegalArgumentException();
        this.cTag = cTag;
    }

    public byte[] getMessageBytes() {
        return this.message;
    }

    public LengthTag getLengthTag() {
        return this.lTag;
    }

    public CheckLengthTag getCLTag() {
        return this.clTag;
    }

    public CheckTag getCheckTag() {
        return this.cTag;
    }

    public String getMessage() {
        return new String(this.message);
    }

    public String getWholeMessage() {
        return lTag.getTag() + clTag.getTag() + new String(message) + cTag.getTag();
    }

    @Override
    public String toString() {
        return this.getWholeMessage();
    }
}
