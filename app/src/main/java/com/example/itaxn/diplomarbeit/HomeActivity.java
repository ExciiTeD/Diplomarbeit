package com.example.itaxn.diplomarbeit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements WaveStegTab.OnFragmentInteractionListener, LiveStegTab.OnFragmentInteractionListener, AboutTab.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.checkAndRequestPermissions();
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Wave~Steg"));
        tabLayout.addTab(tabLayout.newTab().setText("Live~Steg"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isRecordPerm = PermissionManager.checkPermission
                (this, Manifest.permission.RECORD_AUDIO);
        boolean isWritePerm = PermissionManager.checkPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean isReadPerm = PermissionManager.checkPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (!isReadPerm || !isRecordPerm || !isWritePerm) {
            System.exit(1);
        }
    }

    private void checkAndRequestPermissions() {
        boolean isRecordPerm = PermissionManager.checkPermission
                (this, Manifest.permission.RECORD_AUDIO);
        boolean isWritePerm = PermissionManager.checkPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean isReadPerm = PermissionManager.checkPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!isReadPerm || !isRecordPerm || !isWritePerm) {
            ArrayList<String> permissions = new ArrayList<String>();

            if (!isRecordPerm) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (!isWritePerm) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!isReadPerm) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            String[] permissionsArr = new String[permissions.size()];

            for (int i = 0; i < permissionsArr.length; i++) {
                permissionsArr[i] = permissions.get(i);
            }

            PermissionManager.requestPermission(this, permissionsArr);
        }
    }
}
