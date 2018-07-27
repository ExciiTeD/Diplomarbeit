package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import com.example.itaxn.diplomarbeit.audio.Wav;
import com.example.itaxn.diplomarbeit.stego.tag.CheckLengthTag;
import com.example.itaxn.diplomarbeit.stego.tag.CheckTag;
import com.example.itaxn.diplomarbeit.stego.tag.LengthTag;
import com.example.itaxn.diplomarbeit.stego.tag.Message;

import java.io.IOException;
import java.util.zip.CRC32;

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
        this.setCheckLengthTag();
        this.setCheckSum();

        LengthTag lTag = this.msg.getLengthTag();
        CheckLengthTag clTag = this.msg.getCLTag();

        int offset = lTag.getTagLen() + clTag.getTagLen();
        int len = (int) (lTag.getTagVal());

        this.msg.setMessage(this.read(offset, len));
        this.compChecksum();

        return this.msg.getMessageBytes();
    }

    /**
     * Compares the saved checksum with the new
     * generated one from the read message. An
     * <code>IllegalArgumentException</code> will be
     * thrown if the checksumms are not the same.
     */
    protected void compChecksum() {
        CRC32 c = new CRC32();
        c.update(this.msg.getMessageBytes());
        long checksum1 = c.getValue();
        long checksum2 = msg.getCheckTag().getTagVal();
        if (checksum1 != checksum2) {
            throw new IllegalArgumentException("No matching Checksums");
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
     * This methods extracts the check length tag of the manipulated
     * wav file and sets it to the <code>Message</code> object
     * of this class.
     * Implementation: Sets the offset through the known header
     * size adding the Lengthtag length multiple the bits per
     * sample, because the check lengthtag follows the length
     * tag.
     */
    protected void setCheckLengthTag() {
        int j = wav.getHeaderSize() + msg.getLengthTag().getTagLen()
                * wav.getBitsPerSample();
        StringBuffer sBuf = new StringBuffer();

        this.extractTag(sBuf, j);

        String tag = new String(sBuf);
        this.msg.setCLTag(new CheckLengthTag(tag));
    }

    /**
     * This methods extracts the check tag of the manipulated
     * wav file and sets it to the <code>Message</code> object
     * of this class.
     * Implementation: Sets the offset through the know header size
     * adding the lengthtag and checklengthtag length and the length
     * tag value multiplied with the bits per sample value, because the
     * Checktag follows after the first two tags and the message.
     */
    protected void setCheckSum() {
        LengthTag lTag = this.msg.getLengthTag();
        CheckLengthTag clTag = this.msg.getCLTag();
        int j = (int) (wav.getHeaderSize() + (lTag.getTagVal() +
                lTag.getTagLen() + clTag.getTagLen()) * (wav.getBitsPerSample()));
        StringBuffer sBuf = new StringBuffer();

        this.extractTag(sBuf, j);

        String tag = new String(sBuf);
        this.msg.setcTag(new CheckTag(tag));
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
