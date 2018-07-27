package com.example.itaxn.diplomarbeit.audio;

import java.io.IOException;

public class Converter {
    public static void toMono(Wav wav) throws IOException {
        byte[] chan1 = new byte[wav.getPCMData().length/2];
        byte[] chan2 = new byte[wav.getPCMData().length/2];
        wav.seperateChannels(chan1, chan2);
        wav.generateHeaderAndWritePCMData(chan1);
    }
}
