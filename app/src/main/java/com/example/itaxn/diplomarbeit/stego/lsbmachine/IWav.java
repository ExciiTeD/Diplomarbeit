package com.example.itaxn.diplomarbeit.stego.lsbmachine;

import java.io.IOException;

public interface IWav {
    /**
     * Maximum Header size of wav files in Bytes.
     */
    public static final byte MAX_HEADER_SIZE = 78;
    public static final String DATA_CHUNK_ID = "data";

    public void write(byte[] data) throws IOException;

    public void updateContent() throws IOException;

    public byte[] getFileContent();

    public int getFileSize();

    public int getFormatCode();

    public int getBitsPerSample();

    public boolean isFormatPCM();

    public byte getHeaderSize();
}
