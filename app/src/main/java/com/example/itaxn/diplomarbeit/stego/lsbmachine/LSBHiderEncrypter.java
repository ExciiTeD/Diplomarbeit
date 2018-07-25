package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import com.example.itaxn.diplomarbeit.stego.crypto.Crypter;
import com.example.itaxn.diplomarbeit.stego.tag.Message;

import java.io.IOException;


public class LSBHiderEncrypter extends LSBHider {
    private Crypter crypter;

    /**
     * Sets the wav file with the path
     */
    public LSBHiderEncrypter(String wavFile) throws IOException {
        super(wavFile);
        crypter = new Crypter();
    }

    /**
     * Sets the wav file with the wav object
     */
    public LSBHiderEncrypter(Wav wav) throws IOException {
        super(wav);
        crypter = new Crypter();
    }

    /**
     * Hides the data into the wav files lsb's encrypted
     *
     * @param message  the message that should be hidden
     * @param password the password that will be required to
     *                 decrypt the data.
     * @throws Exception a lot of exceptions.
     */
    public void hideEncrypted(String message, String password) throws Exception {
        this.msg = new Message(message.getBytes());
        byte[] unencryptedData = this.msg.getWholeMessage().getBytes();
        byte[] encryptedData = crypter.encode(password, unencryptedData); //encrypting the data

        if (this.messageFits(encryptedData)) {
            byte[] hC = this.generateHiddenContent(encryptedData,
                    wav.getFileContent(), wav.getHeaderSize()); //Generate new Content with the encrypted data
            this.writeHiddenContent(hC); //Write the new Content to the file
        } else {
            throw new MessageTooLongException();
        }
    }
}
