package com.example.itaxn.diplomarbeit.fft;

import org.jtransforms.fft.DoubleFFT_1D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FastFourierTransformation {
    private DoubleFFT_1D fft;
    private double[] soundData;
    private double[] fftData;

    public FastFourierTransformation(double[] soundData) {
        if (isPowerOfTwo(soundData.length)) {
            throw new IllegalArgumentException("The array length has to be a power of two");
        }
        this.soundData = soundData;
        this.fft = new DoubleFFT_1D(soundData.length);
        this.fftData = new double[soundData.length * 2];
    }

    public double[] doFFT() {
        System.arraycopy(soundData, 0, fftData, 0, soundData.length);
        this.fft.realForwardFull(fftData);
        return fftData;
    }

    public double[] doComplexFFT() {
        this.doFFT();
        double[] fftValues = new double[fftData.length / 4];

        for (int i = 0; i < fftData.length / 4; i++) {
            fftValues[i] = Math.hypot(fftData[i * 2], fftData[i * 2 + 1]) / 44100 / 16;
        }

        return fftValues;
    }

    public void inverse() {
        fft.complexInverse(fftData, true);
    }

    public double[] inverse(double[] fftData) {
        fft.complexInverse(fftData, true);
        return fftData;
    }

    /**
     * Converts a 16 bit per sample pcm data aaray to an double array
     * @param data
     * @return
     */
    public static double[] bytesToDoubleArray(byte[] data, int length) {
        double[] dArr = new double[length];
        short[] shorts = new short[data.length/2];
        ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

        for (int i = 0; i < length; i++)
            dArr[i] = shorts[i];
        return dArr;
    }

    private static boolean isPowerOfTwo(int number) {
        if (number % 2 != 0 || number <= 0) {
            return false;
        } else {

            for (int i = 0; i <= number; i++) {
                if (Math.pow(2, i) == number) return true;
            }
        }
        return false;
    }
}
