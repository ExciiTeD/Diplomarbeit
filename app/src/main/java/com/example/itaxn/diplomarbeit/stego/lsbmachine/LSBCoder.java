package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import com.example.itaxn.diplomarbeit.audio.Wav;
import com.example.itaxn.diplomarbeit.stego.tag.Message;

import java.io.IOException;

public abstract class LSBCoder {
    /**
     * Wav File which should be manipulated through
     * a LSBCoder subclass.
     */
    protected Wav wav;

    /**
     * The message that contains the tags and the message
     * that should be hidden.
     */
    protected Message msg;

    /**
     * Creates a new LSBCoder and wav object on the given path
     *
     * @param wavFile path to the wav file
     * @throws IOException
     */
    public LSBCoder(String wavFile) throws IOException {
        if (wavFile == null) {
            throw new IllegalArgumentException("Null is not allowed");
        }
        wav = new Wav(wavFile);
        msg = null;
    }

    /**
     * Creates a new LSBCoder object and initializes the wav object
     *
     * @param wav object for reading and writing
     */
    public LSBCoder(Wav wav) {
        if (wav == null) {
            throw new IllegalArgumentException("Null is not allowed");
        }
        this.wav = wav;
        msg = null;
    }
}
