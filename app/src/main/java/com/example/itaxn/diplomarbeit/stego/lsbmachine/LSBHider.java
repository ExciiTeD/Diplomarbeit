package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import java.io.IOException;

import com.example.itaxn.diplomarbeit.audio.Wav;
import com.example.itaxn.diplomarbeit.stego.tag.Message;

/**
 * The LSBHider class is a class that can hide (write) a
 * message into the least significant bits of a wav file.
 *
 * @author Poehli.a
 */
public class LSBHider extends LSBCoder {
    /**
     * Sets the wav file of the LSBHider with the
     * given path.
     *
     * @param wavFile Path to the wav file.
     * @throws IOException
     */
    public LSBHider(String wavFile) throws IOException {
        super(wavFile);
    }

    /**
     * Set the wav file of the LSBHider with
     * the given wav Object.
     *
     * @param wav object that should be manipulated
     * @throws IOException
     */
    public LSBHider(Wav wav) throws IOException {
        super(wav);
    }

    /**
     * This method hides a message into the wav file by inserting data into the
     * LSB's of the bytes. If the data is to big for this file it throws
     * an <code>IllegalargumentException</code>.
     *
     * @param message Message as a String that should be hidden into the wav file
     * @throws IOException if an IO error occurs
     */
    public void hide(String message) throws IOException, MessageTooLongException {
        this.msg = new Message(message.getBytes());
        byte[] data = msg.getWholeMessage().getBytes(); //Put the bytes of the tags and the message into an array
        byte[] wavContent = wav.getFileContent();    //Putting the file content into a byte array

        /*Asking if the messages fits into the wav files lsb's. */
        if (this.messageFits(data)) {
            byte[] hC = this.generateHiddenContent(data,
                    wavContent, wav.getHeaderSize()); //Generate new Content
            this.writeHiddenContent(hC); //Write the new Content to the file
        } else {
            throw new MessageTooLongException();
        }
    }

    /**
     * Proofs if the message fits into the file
     *
     * @param data (+tags) that should be hidden in the wav file.
     * @return true if the message would fit into the wav file otherwise false
     */
    protected boolean messageFits(byte[] data) {
        int avaiableSize = (wav.getFileSize() - wav.getHeaderSize())
                / wav.getBitsPerSample() - 16; //-16 because of the iv
        return data.length < avaiableSize;
    }

    /**
     * Method that writes the new Generated Content to the wav file
     *
     * @param hC the new hidden content
     * @throws IOException if any IO error occurs
     */
    protected void writeHiddenContent(byte[] hC) throws IOException {
        wav.write(hC);
    }

    /**
     * Generates the hidden Content with the LSB algorithm.
     *
     * @param data The data that should be hidden
     * @param wav  the content of the given wav file
     * @return a byte Array of the new File content
     */
    protected byte[] generateHiddenContent(byte[] data, byte[] wav, byte headerSize) {
        int i = headerSize; //set the iterator of wav to Header size
        int j = 0; //iterator of data array

        /*the execution of this code lasts so long as one of the iterators are
         * bigger than their array length */
        while (j < data.length) {
            int k = 0x80; // create variable with the Most significant bit as 1
            // repeating this code 8 times to extract every bit of the data
            for (int l = 0; l < 8; l++) {
                int comp = data[j] & k; //Get a bit of the data byte by using the bitwise and operator

                if (comp != 0) {
                    //if this bit is set 1 the lsb of a byte of the wav becomes 1 by an or operation with 1
                    wav[i] = (byte) (wav[i] | 0x01);
                } else {
                    //if this bit is zero the lsb of a byte of the wav becomes zero by an and operation with 0xFE
                    wav[i] = (byte) (wav[i] & 0xFE);
                }
                i += (this.wav.getBitsPerSample() / 8); //switch the byte of the wav array
                k = k >> 1; //right shift operation to extract the next bit
            }
            j++; //switch the byte of the data array
        }
        return wav;
    }
}
