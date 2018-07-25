package com.example.itaxn.diplomarbeit.fft;

import org.jtransforms.fft.DoubleFFT_1D;

public class FastFourierTransformation {
    private DoubleFFT_1D fft;
    private double[] soundData;
    private double[] fftData;

    public FastFourierTransformation(double[] soundData) {
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
}
