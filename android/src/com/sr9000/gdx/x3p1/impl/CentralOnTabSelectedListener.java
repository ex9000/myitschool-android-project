package com.sr9000.gdx.x3p1.impl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.sr9000.gdx.x3p1.GDXFragment;
import com.sr9000.gdx.x3p1.MainActivity;
import com.sr9000.gdx.x3p1.R;
import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.tabs.CreditsTabFragment;
import com.sr9000.gdx.x3p1.tabs.SettingsTabFragment;

import java.util.concurrent.Callable;

public class CentralOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
    private final AppCompatActivity parentActivity;


    public CentralOnTabSelectedListener(AppCompatActivity parent) {
        parentActivity = parent;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (!AppBusiness.INSTANCE.is_resumed()) {
            return;
        }

        Fragment f = null;
        switch (tab.getPosition()) {
            case 0:
                //f = new OverviewTabFragment();
                f = new GDXFragment();
                break;

            case 1:
                f = new SettingsTabFragment();
                break;

            case 2:
                f = new CreditsTabFragment();
                break;

            default:
                return;
        }

        FragmentManager fm = parentActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFrameLayout, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        AppBusiness.INSTANCE.getGuiStatus().selected_tab = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
