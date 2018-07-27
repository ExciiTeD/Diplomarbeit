package com.example.itaxn.diplomarbeit.audio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for Wav files. Purpose is to
 * get the content of the designated file or
 * manipulate the content
 *
 * @author Poehli.a
 */
public class Wav implements IWav {
    private byte headerSize;

    private File wavFile;    //The wav file
    private byte[] fileContent; //The whole content of file in a buffer
    private int numChannels;
    private int bitsPerSample; //contains bit per sample
    private int formatCode; //format code of wave file
    private int size;    //File size of the wav file

    /**
     * @param pathname of the wav file
     * @throws IOException
     */
    public Wav(String pathname) throws IOException {
        this.setFileByPathname(pathname);
        this.init();
    }

    /**
     * Creating a wav object with an Inputstream.
     * @param inputStream of the wav file.
     * @param size of the wav file in bytes
     */
    public Wav(InputStream inputStream, long size) throws IOException {
        this.initWithStream(inputStream, size);
    }

    /**
     * initializes the attributes of this class.
     *
     * @throws IOException if an IO error occurs during the file
     *                     reading.
     */
    private void init() throws IOException {
        this.size = (int) wavFile.length();
        this.fileContent = new byte[this.size];
        this.readData();
        this.numChannels = this.fileContent[22];
        this.bitsPerSample = this.fileContent[34];
        this.formatCode = this.fileContent[20];
        this.initHeaderSize();
    }

    private void initWithStream(InputStream inputStream, long size) throws IOException {
        this.size = (int) size;
        this.fileContent = new byte[this.size];
        this.readData(inputStream);
        this.numChannels = this.fileContent[22];
        this.bitsPerSample = this.fileContent[34];
        this.formatCode = this.fileContent[20];
        this.initHeaderSize();
    }

    /**
     * Reads the content of the wav file with the given
     * Inputstrem.
     * @param inputStream of a wav file
     * @throws IOException
     */
    private synchronized void readData(InputStream inputStream) throws IOException{
        BufferedInputStream bis = new BufferedInputStream(inputStream, 5);
        bis.read(this.fileContent);
        bis.close();
    }

    /**
     * this method gives you the pcm data of the wav file.
     * that means the method returns the file content without
     * the header or the header information.
     * @return the pcm data of the wav file in bytes.
     */
    public byte[] getPCMData() {
        byte[] pcmData = new byte[fileContent.length - this.getHeaderSize()];
        System.arraycopy(fileContent, this.getHeaderSize(), pcmData, 0, pcmData.length);
        return pcmData;
    }

    /**
     * reads the Data of the wav file.
     *
     * @throws IOException
     */
    private synchronized void readData() throws IOException {
        FileInputStream fis = new FileInputStream(this.wavFile);
        BufferedInputStream bis = new BufferedInputStream(fis, 5);
        bis.read(this.fileContent);
        bis.close();
    }

    /**
     * initializes the header size of this wav file by reading
     * the first 78 bytes. Then a <code>Stringbuffer</code> will save it,
     * By iterating over string buffer the method looks up for the data
     * tag. After the tag was found the method sets the header size.
     *
     * @return if the tag was found (success) or not (failure)
     */
    private boolean initHeaderSize() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < Wav.MAX_HEADER_SIZE; i++) {
            sb.append((char) (this.fileContent[i]));
        }

        for (int i = 0; i < (sb.length() - 4); i++) {
            String str = sb.substring(i, (i + 4));
            if (str.equals("data")) {
                this.headerSize = (byte) (i + 8);
                return true;
            }
        }

        return false;
    }

    /**
     * Use this method after the file content was changed.
     * The content buffer of this class will be updated.
     *
     * @throws IOException
     */
    public void updateContent() throws IOException {
        this.readData();
    }

    /**
     * Sets the wav file through the given pathname.
     *
     * @param pathname path to the wav file!
     * @throws FileNotFoundException
     */
    public void setFileByPathname(String pathname) throws FileNotFoundException {
        StringBuffer path = new StringBuffer(pathname);
        if (!pathname.endsWith(".wav")) {
            path.append(".wav");
        }

        File file = new File(path.toString());

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        this.wavFile = file;
    }

    /**
     * Write data to the Wav file. The old data will be deleted
     * after calling this method.
     *
     * @param data Data in bytes that will be written into the wav file.
     * @throws IOException Error that could occur while writing to the file
     */
    public synchronized void write(byte[] data) throws IOException {
        FileOutputStream fo = new FileOutputStream(wavFile);
        BufferedOutputStream bo = new BufferedOutputStream(fo, 5);
        bo.write(data);
        bo.flush();
        bo.close();
    }



    public void generateHeaderAndWriteWithPCMData(byte[] chan1) {

    }

    /**
     *
     * @param ch1
     * @param ch2
     */
    public void seperateChannels(byte[] ch1, byte[] ch2){
        if(this.getNumChannels()==2){
            int bytesPerSample = this.getBitsPerSample()/8;
            byte[] pcmData = this.getPCMData();
            int indexCh1=0;
            int indexCh2=0;
            boolean changeCh = false;
            for(int i=0; i<pcmData.length; i++){
                if((i%bytesPerSample)==0){
                    changeCh ^= true;
                }
                if(changeCh){
                    ch1[indexCh1]=pcmData[i];
                    indexCh1++;
                }
                else{
                    ch2[indexCh2]=pcmData[i];
                    indexCh2++;
                }
            }
        }
    }

    /**
     * @return a copy of the fileContent buffer
     */
    public byte[] getFileContent() {
        return fileContent.clone();
    }

    /**
     * @return the file size in Bytes as an Integer
     */
    public int getFileSize() {
        return this.size;
    }

    /**
     * @return Format Code of Wav file
     */
    public int getFormatCode() {
        return this.formatCode;
    }

    /**@return the number of channels in the wav file.*/
    public int getNumChannels() { return this.numChannels; }

    /**
     * returns the amount of bits per sample
     */
    public int getBitsPerSample() {
        return this.bitsPerSample;
    }

    /**
     * asks if the wav file contains pcm data
     */
    public boolean isFormatPCM() {
        return this.formatCode == 0x01;
    }

    /**
     * returns header size of file in bytes
     */
    public byte getHeaderSize() {
        return this.headerSize;
    }

    @Override
    public String toString() {
        return "Path: " + this.wavFile.getPath() + "\nSize: " + this.size + " bytes\n";
    }
}
