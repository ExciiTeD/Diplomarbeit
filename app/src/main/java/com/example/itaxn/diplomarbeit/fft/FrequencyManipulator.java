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
    private double[] dataSin20khz;
    private double[] dataSin21khz;
    private double[] fftDatasin20;
    private double[] fftDatasin21;

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
        long filesize1 = this.context.getResources().openRawResourceFd(R.raw.sin_20khz).getLength();
        long filesize2 = this.context.getResources().openRawResourceFd(R.raw.sin_21khz).getLength();
        Wav sin20khz = new Wav(inputStream1, filesize1);
        Wav sin21khz = new Wav(inputStream2, filesize2);
        this.dataWav = FastFourierTransformation.bytesToDoubleArray(
                wavToManipulate.getFileContent(), wavToManipulate.getFileSize());
        this.dataSin20khz = FastFourierTransformation
                .bytesToDoubleArray(sin20khz.getFileContent(), FFT_SIZE);
        this.dataSin21khz = FastFourierTransformation
                .bytesToDoubleArray(sin21khz.getFileContent(), FFT_SIZE);
        this.doFFTOnFrequencies();
    }

    private void doFFTOnFrequencies() {
        FastFourierTransformation fft = new FastFourierTransformation(dataSin20khz);
        this.fftDatasin20 = fft.doFFT();
        fft = new FastFourierTransformation(dataSin21khz);
        this.fftDatasin21 = fft.doFFT();
    }

    /**
     *
     * @param offset the offset sample were the frequency should be addes to wav file.
     */
    public void add20khzSine(int offset) {
        if (!contains20khz) {
            double[] sampleData = new double[FFT_SIZE];
            System.arraycopy(dataWav, offset * FFT_SIZE, sampleData, 0, FFT_SIZE);
            double[] fftData = new FastFourierTransformation(sampleData).doFFT();
            this.insertFrequency20khz(fftData);
            //new FastFourierTransformation();
        }
    }

    private void insertFrequency20khz(double[] fftData) {
        for (int i = 0; i < fftData.length/2;i++) {
            int frequency = i * (Wav.SAMPLE_RATE/FFT_SIZE);
            if (frequency > 19800 && frequency < 20200) {
                fftData[i*2] = fftDatasin20[i*2];
                fftData[i*2+1] = fftDatasin20[i*2+1];
                int j = getOppositeValue(fftData, i*2);
                fftData[j] = fftDatasin20[j];
                fftData[j+1] = fftDatasin20[j+1];
            }
        }
    }

    public void add21khzSine() {
        if (!contains21khz) {

        }
    }

    public static int getOppositeValue(double[] fftData, int position) {
        if (position >= fftData.length/2 || position % 2 != 0) {
            throw new IllegalArgumentException("wrong position");
        }
        int dif = fftData.length/2 - position;
        return fftData.length/2 + dif;
    }
}

