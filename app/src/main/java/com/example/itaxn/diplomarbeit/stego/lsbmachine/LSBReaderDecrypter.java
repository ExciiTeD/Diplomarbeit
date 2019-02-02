package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import com.example.itaxn.diplomarbeit.audio.Wav;
import com.example.itaxn.diplomarbeit.stego.crypto.Crypter;
import com.example.itaxn.diplomarbeit.stego.tag.CheckLengthTag;
import com.example.itaxn.diplomarbeit.stego.tag.CheckTag;
import com.example.itaxn.diplomarbeit.stego.tag.LengthTag;

import java.io.IOException;


public class LSBReaderDecrypter extends LSBReader {
    private Crypter crypter;
    private byte[] partOfMessage;

    /**
     * Creates a new LSBCoder and wav object on the given path
     *
     * @param wav path to the wav file
     * @throws IOException
     */
    public LSBReaderDecrypter(Wav wav) throws IOException {
        super(wav);
        this.crypter = new Crypter();
    }

    /**
     * Creates a new LSBCoder object and initializes the wav object
     *
     * @param wavFile object for reading and writing
     */
    public LSBReaderDecrypter(String wavFile) throws IOException {
        super(wavFile);
        this.crypter = new Crypter();
    }

    /**
     * This method reads and decodes only a part of the message.
     * Of course you can read the whole message if you
     * pass on the right length.
     *
     * @param password The password that is required to decode the Message.
     * @param length   the length of the message
     * @return the decoded message in bytes
     * @throws Exception
     */
    private byte[] readPartOfMessage(String password, int length) throws Exception {
        length += 32; //add 32 because 16 bytes are needed for the salt and 16 for the init vector
        int remain = length % 16;
        if (remain != 0) {    //test if the length the message consists of 16 byte blocks
            length += 16 - remain; //add the number that is necessary to get to a 16 byte block and
        }
        return crypter.decode(password, read(0, length));
    }

    /**
     * Decrypts and Reads the whole hidden message by
     * decrypting the length tag and the check length tag
     * at first. Afterwards the method decrypts
     * the whole message based on the value
     * of the length tag and the check length tag. Finally
     * the method proofs the checksum and if it was a success the
     * method will return the message. Otherwise a
     * <code>IllegalArgumentException</code> will be thrown.
     *
     * @param password that is needed to decrypt the hidden data
     * @return the plain text message in bytes.
     * @throws Exception
     */
    public byte[] readMessage(String password) throws Exception {
        this.partOfMessage = this.readPartOfMessage(password, 64);
        this.setLengthTag();
        this.setCheckLengthTag();
        LengthTag lTag = this.msg.getLengthTag();
        CheckLengthTag clTag = this.msg.getCLTag();
        int msgLength = (int) (lTag.getTagLen() + clTag.getTagLen() +
                lTag.getTagVal() + clTag.getTagVal());
        this.partOfMessage = this.readPartOfMessage(password, msgLength);
        this.setCheckSum();
        this.setMessage();
        this.compChecksum();
        return this.msg.getMessageBytes();
    }

    /**
     * this method sets the message of the <code>Message</code> object by
     * copy a part of the <code>parOfMessage</code> array.
     * Offset is the lengthtag length plus the checklength length
     * and the length is the value of the length tag.
     */
    private void setMessage() {
        LengthTag lTag = this.msg.getLengthTag();
        CheckLengthTag clTag = this.msg.getCLTag();
        byte[] message = new byte[(int) lTag.getTagVal()];
        System.arraycopy(partOfMessage, lTag.getTagLen() +
                clTag.getTagLen(), message, 0, (int) lTag.getTagVal());
        this.msg.setMessage(message);
    }

    /**
     * Sets the <code>LengthThag</code> of the message object
     * by extracting the first characters of the
     * <code>partOfMessage</code> array that were encrypted
     * before. That's why the offset is 0,
     * If the encryption failed you will probably get an
     * Exception.
     */
    @Override
    protected void setLengthTag() {
        StringBuffer sBuf = new StringBuffer();
        this.extractTag(sBuf, 0);
        this.msg.setlTag(new LengthTag(sBuf.toString()));
    }

    /**
     * Sets the <code>CheckLengthTag</code> of the message object
     * by extracting the first characters after the length tag of the
     * <code>partOfMessage</code> array that were encrypted
     * before. That's why the offset is the <code>LengthTag</code> length.
     * If the encryption failed you will probably get an Exception.
     */
    @Override
    protected void setCheckLengthTag() {
        StringBuffer sBuf = new StringBuffer();
        this.extractTag(sBuf, this.msg.getLengthTag().getTagLen());
        this.msg.setCLTag(new CheckLengthTag(sBuf.toString()));
    }

    /**
     * Sets the <code>CheckTag</code> of the message object by
     * extracting the last characters of the new encrypted message.
     * That's why the offset consists of <code>LengthTag</code> length
     * and value and the <code>CheckLengthTag</code> length.
     */
    @Override
    protected void setCheckSum() {
        StringBuffer sBuf = new StringBuffer();
        LengthTag lTag = this.msg.getLengthTag();
        CheckLengthTag clTag = this.msg.getCLTag();
        int offset = (int) (lTag.getTagLen() + clTag.getTagLen() + lTag.getTagVal());
        this.extractTag(sBuf, offset);
        this.msg.setcTag(new CheckTag(sBuf.toString()));
    }

    /**
     * Extracts a tag by adding characters to the StringBuffer
     * if a '<' appears and stops adding characters if an '>' appears
     */
    @Override
    protected void extractTag(StringBuffer sBuf, int j) {
        char c = ' ';
        boolean tagStarted = false;

        for (int i = j; i < partOfMessage.length; i++) {
            c = (char) partOfMessage[i];

            if (c == '<') {
                sBuf.append(c);
                tagStarted = true;
            } else if (tagStarted) {
                sBuf.append(c);
                if (c == '>') {
                    break;
                }
            }
        }
    }
}
