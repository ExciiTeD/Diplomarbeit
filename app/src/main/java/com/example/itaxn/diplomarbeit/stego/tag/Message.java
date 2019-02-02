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
     * The hash tag of the message.
     */
    private HashTag hTag;

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
        this.hTag = new HashTag(this.message);
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
     * @param hTag, not null
     */
    public void sethTag(HashTag hTag) {
        if (hTag == null)
            throw new IllegalArgumentException();
        this.hTag = hTag;
    }

    public byte[] getMessageBytes() {
        return this.message;
    }

    public LengthTag getLengthTag() {
        return this.lTag;
    }

    public HashTag getHashTag() {
        return this.hTag;
    }

    public String getMessage() {
        return new String(this.message);
    }

    public String getWholeMessage() {
        return lTag.getTag() + new String(message) + hTag.getTag();
    }

    @Override
    public String toString() {
        return this.getWholeMessage();
    }
}
