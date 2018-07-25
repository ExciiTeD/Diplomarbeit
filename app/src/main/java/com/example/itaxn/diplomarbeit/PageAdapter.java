package com.example.itaxn.diplomarbeit;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                WaveStegTab waveSteg = new WaveStegTab();
                return waveSteg;

            case 1:
                LiveStegTab liveStegTab = new LiveStegTab();
                return liveStegTab;

            case 2:
                AboutTab aboutTab = new AboutTab();
                return aboutTab;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }
}