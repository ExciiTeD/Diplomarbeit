package com.example.itaxn.diplomarbeit.audio;

public class Converter {
    public static void toMono(Wav wav) {
        byte[] chan1 = new byte[wav.getPCMData().length/2];
        byte[] chan2 = new byte[wav.getPCMData().length/2];
        wav.seperateChannels(chan1, chan2);
        wav.generateHeaderAndWriteWithPCMData(chan1);
    }
}
