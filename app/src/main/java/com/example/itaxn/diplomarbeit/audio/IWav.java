package com.example.itaxn.diplomarbeit.audio;

import java.io.IOException;

public interface IWav {
    /**
     * Maximum Header size of wav files in Bytes.
     */
    byte MAX_HEADER_SIZE = 78;
    int SAMPLE_RATE = 44100;
    short MONO = 1;
    short STEREO = 2;

    public void write(byte[] data) throws IOException;

    public void updateContent() throws IOException;

    public byte[] getFileContent();

    public int getFileSize();

    public int getFormatCode();

    public int getBitsPerSample();

    public boolean isFormatPCM();

    public byte getHeaderSize();
}
