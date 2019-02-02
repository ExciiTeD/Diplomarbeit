package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import com.example.itaxn.diplomarbeit.audio.Wav;
import com.example.itaxn.diplomarbeit.stego.tag.HashTag;
import com.example.itaxn.diplomarbeit.stego.tag.LengthTag;
import com.example.itaxn.diplomarbeit.stego.tag.Message;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The class LSBReader is a class that can extract
 * the least significant bits and put them together to
 * a byte array.
 *
 * @author Poehli.a
 */
public class LSBReader extends LSBCoder {
    /**
     * Sets the wav file of the LSBCoder with the
     * given path.
     *
     * @param wavFile Path to the wav file.
     * @throws IOException
     */
    public LSBReader(String wavFile) throws IOException {
        super(wavFile);
        this.msg = new Message();
    }

    /**
     * Sets the wav file of the LSBCoder with the
     * given object.
     *
     * @param wav object.
     * @throws IOException
     */
    public LSBReader(Wav wav) throws IOException {
        super(wav);
        this.msg = new Message();
    }

    /**
     * reads the hidden message of the Wav file and updates
     * the content in the wav object content buffer. If there
     * isn't any secret message you probably get a unreadable
     * String as return value.
     *
     * @return the extracted message as a byte array
     * @throws IOException if any IOError occurs
     */
    protected byte[] read(int offset, int length) throws Exception {
        byte[] content = wav.getFileContent();
        byte[] message = new byte[length];
        int j = wav.getHeaderSize() + (offset * wav.getBitsPerSample());

        for (int i = 0; i < length; i++) {
            for (int k = 7; k >= 0; k--, j += (wav.getBitsPerSample() / 8)) {
                byte lsb = (byte) (content[j] & 0x01); //get the lsb by making an and operation with 0x01
                lsb <<= k;    //shift the bit to the right position
                message[i] |= lsb; //making an or operation to set the lsb to the right position
            }
        }
        this.msg.setMessage(message); //set the message to the message object.

        return message;
    }

    /**
     * Reads the message of the manipulated wav file and updates
     * the content buffer of this wav object. At first the tags
     * will be read to get the length of the message and the
     * checksum to call the {@code read(offset, length)} method
     * properly. Offset and length will be calculated by the tags.
     * Finally the message will be compared with the checksum to
     * detect errors in contribution to the message.
     *
     * @return the message as a byte array.
     * @throws Exception if any IO-Error occurs
     */
    public byte[] read() throws Exception {
        this.setLengthTag();
        this.setHash();

        LengthTag lTag = this.msg.getLengthTag();
        HashTag hTag = this.msg.getHashTag();

        int offset = lTag.getTagLen();
        int len = (int) (lTag.getTagVal());

        this.msg.setMessage(this.read(offset, len));
        this.compHash();

        return this.msg.getMessageBytes();
    }

    /**
     * Compares the saved checksum with the new
     * generated one from the read message. An
     * <code>IllegalArgumentException</code> will be
     * thrown if the checksumms are not the same.
     */
    protected void compHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(this.msg.getMessageBytes());
            String hash1 = HashTag.toHex(md.digest());
            String hash2 = this.msg.getHashTag().getHexVal();
            if (!hash1.equals(hash2)) {
                throw new IllegalArgumentException("No matching Hashes");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * This methods extracts the length tag of the manipulated
     * wav file and sets it to the <code>Message</code> object
     * of this class.
     * Implementation: the offset will be set through the
     * known headersize because the length tag is the first
     * thing that will be hidden after the header.
     */
    protected void setLengthTag() {
        int j = wav.getHeaderSize();
        StringBuffer sBuf = new StringBuffer();

        this.extractTag(sBuf, j);

        String tag = new String(sBuf);
        this.msg.setlTag(new LengthTag(tag));
    }

    /**
     * This methods extracts the hash tag of the manipulated
     * wav file and sets it to the <code>Message</code> object
     * of this class.
     * Implementation: the offset will be set through the
     * known headersize, the tagsize of the lengthtag plus the
     * value of the lengthtag, because rigth after the message is
     * the hashtag placed.
     */
    protected void setHash() {
        LengthTag lTag = this.msg.getLengthTag();
        int j = (int) (wav.getHeaderSize() + (lTag.getTagVal() + lTag.getTagLen())
                * wav.getBitsPerSample());
        StringBuffer sBuf = new StringBuffer();

        this.extractTag(sBuf, j);

        String tag = sBuf.toString();
        this.msg.sethTag(new HashTag(tag));
    }

    /**
     * Extracts the tag by saving all charcter after the '<'
     * and stop saving them if an '>' occurs. at the given
     * offset called j. The tag will be saved in to the
     * string buffer so watch your
     * instance you will pass on this method.
     *
     * @param sBuf tag will be saved into this instance
     * @param j    the offset where the method starts to read.
     */
    protected void extractTag(StringBuffer sBuf, int j) {
        byte b = 0;
        char c = ' ';
        boolean tagStarted = false;

        do {
            b = 0;
            for (int k = 7; k >= 0; k--, j += (wav.getBitsPerSample() / 8)) {
                byte lsb = (byte) (wav.getFileContent()[j] & 0x01);
                lsb <<= k;
                b |= lsb;
            }
            c = (char) b;
            if (c == '<') {
                tagStarted = true;
                sBuf.append((char) b);
            } else if (tagStarted) {
                sBuf.append((char) b);
            }
        } while ((char) (b) != '>');
    }

    /**
     * The same implementation as <code>read()</code> with the difference
     * that you get a String as return value
     *
     * @return String of the hidden message if available
     * @throws IOException
     */
    public String readString() throws Exception {
        return new String(this.read());
    }
}
