package com.example.itaxn.diplomarbeit.fft;

import android.content.Context;

import com.example.itaxn.diplomarbeit.R;
import com.example.itaxn.diplomarbeit.stego.lsbmachine.Wav;

import java.io.IOException;
import java.io.InputStream;

public class FrequencyManipulator {
    public static final int FFT_SIZE = 4096;
    private Context context;
    private Wav wavToManipulate;
    private double[] dataWav;
    private double[] fftDataSin20;
    private double[] fftDataSin21;

    private boolean contains20khz;
    private boolean contains21khz;

    public FrequencyManipulator(Context context, Wav wav) throws IOException {
        this.context = context;
        this.wavToManipulate = wav;
        this.contains20khz = false;
        this.contains21khz = false;
        this.initFrequencies();
    }

    private void initFrequencies() throws IOException {
        InputStream inputStream1 = this.context.getResources().openRawResource(R.raw.sin_20khz);
        InputStream inputStream2 = this.context.getResources().openRawResource(R.raw.sin_21khz);
        long fileSize1 = this.context.getResources().openRawResourceFd(R.raw.sin_20khz).getLength();
        long fileSize2 = this.context.getResources().openRawResourceFd(R.raw.sin_21khz).getLength();
        Wav sin20khz = new Wav(inputStream1, fileSize1);
        Wav sin21khz = new Wav(inputStream2, fileSize2);
        this.dataWav = FastFourierTransformation.bytesToDoubleArray(
                wavToManipulate.getFileContent(), wavToManipulate.getFileSize());
        double[] dataSin20khz = FastFourierTransformation
                .bytesToDoubleArray(sin20khz.getFileContent(), FFT_SIZE);
        double[] dataSin21khz = FastFourierTransformation
                .bytesToDoubleArray(sin21khz.getFileContent(), FFT_SIZE);
        this.doFFTOnFrequencies(dataSin20khz, dataSin21khz);
    }

    private void doFFTOnFrequencies(double[] dataSin20khz, double[] dataSin21khz) {
        FastFourierTransformation fft = new FastFourierTransformation(dataSin20khz);
        this.fftDataSin20 = fft.doFFT();
        fft = new FastFourierTransformation(dataSin21khz);
        this.fftDataSin21 = fft.doFFT();
    }

    /**
     *
     * @param offset the offset sample were the frequency should be added to wav file.
     */
    public void add20khzSine(int offset) {
        if (!contains20khz) {
            double[] sampleData = new double[FFT_SIZE];
            System.arraycopy(dataWav, offset * FFT_SIZE, sampleData, 0, FFT_SIZE);
            FastFourierTransformation fft = new FastFourierTransformation(sampleData);
            double[] fftData = fft.doFFT();
            this.insertFrequency20khz(fftData);
            this.addToRawData(fft.inverse(fftData), offset);
        }
    }

    private void addToRawData(double[] inverse, int offset) {
        for (int i = offset; i < offset + FFT_SIZE; i++) {
            dataWav[i] = inverse[i];
        }
    }

    private void insertFrequency20khz(double[] fftData) {
        for (int i = 0; i < fftData.length/2;i++) {
            int frequency = i * (Wav.SAMPLE_RATE/FFT_SIZE);
            if (frequency > 19800 && frequency < 20200) {
                fftData[i*2] = fftDataSin20[i*2];
                fftData[i*2+1] = fftDataSin20[i*2+1];
                int j = getOppositeValue(fftData, i*2);
                fftData[j] = fftDataSin20[j];
                fftData[j+1] = fftDataSin20[j+1];
            }
        }
    }

    public void add21khzSine(int offset) {
        if (!contains21khz) {
            double[] sampleData = new double[FFT_SIZE];
            System.arraycopy(dataWav, offset * FFT_SIZE, sampleData, 0, FFT_SIZE);
            FastFourierTransformation fft = new FastFourierTransformation(sampleData);
            double[] fftData = fft.doFFT();
            this.insertFrequency21khz(fftData);
            fft.inverse(fftData);
            this.addToRawData(fft.inverse(fftData), offset);
        }
    }

    private void insertFrequency21khz(double[] fftData) {
        for (int i = 0; i < fftData.length/2;i++) {
            int frequency = i * (Wav.SAMPLE_RATE/FFT_SIZE);
            if (frequency > 20800 && frequency < 21200) {
                fftData[i*2] = fftDataSin21[i*2];
                fftData[i*2+1] = fftDataSin21[i*2+1];
                int j = getOppositeValue(fftData, i*2);
                fftData[j] = fftDataSin21[j];
                fftData[j+1] = fftDataSin21[j+1];
            }
        }
    }

    private static int getOppositeValue(double[] fftData, int position) {
        if (position >= fftData.length/2 || position % 2 != 0) {
            throw new IllegalArgumentException("wrong position");
        }
        int dif = fftData.length/2 - position;
        return fftData.length/2 + dif;
    }
}

