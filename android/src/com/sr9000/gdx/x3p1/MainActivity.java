package com.sr9000.gdx.x3p1;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.material.tabs.TabLayout;
import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.impl.CentralOnTabSelectedListener;
import com.sr9000.gdx.x3p1.impl.OnSwipe;
import com.sr9000.gdx.x3p1.log.Logger;
import com.sr9000.gdx.x3p1.motion.Motion;
import com.sr9000.gdx.x3p1.provider.PreferencesProvider;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    OnSwipe swipeListener;
    FrameLayout mainFrameLayout;
    TabLayout centralTabLayout;
    Motion motion;
    int init_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        motion = new Motion(getApplicationContext());
        resume_app_business();

        setContentView(R.layout.activity_main);

        mainFrameLayout = findViewById(R.id.mainFrameLayout);
        centralTabLayout = findViewById(R.id.centralTabLayout);

        centralTabLayout.addOnTabSelectedListener(new CentralOnTabSelectedListener(this));
        centralTabLayout.selectTab(centralTabLayout.getTabAt(1));

        swipeListener = new OnSwipe(this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                swipe_right();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                swipe_left();
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        swipeListener.onTouch(event);
        return super.dispatchTouchEvent(event);
    }

    private void swipe_left() {
        AppBusiness.INSTANCE.getGuiStatus().selected_tab = centralTabLayout.getSelectedTabPosition();


        int i = AppBusiness.INSTANCE.getGuiStatus().selected_tab;
        i++;
        if (i >= centralTabLayout.getTabCount()) {
            i = centralTabLayout.getTabCount() - 1;
        }

        centralTabLayout.selectTab(centralTabLayout.getTabAt(i));
    }

    private void swipe_right() {
        AppBusiness.INSTANCE.getGuiStatus().selected_tab = centralTabLayout.getSelectedTabPosition();

        int i = AppBusiness.INSTANCE.getGuiStatus().selected_tab;
        i--;
        if (i < 0) {
            i = 0;
        }

        centralTabLayout.selectTab(centralTabLayout.getTabAt(i));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (!AppBusiness.INSTANCE.is_resumed()) {
            resume_app_business();
        }
    }

    private void resume_app_business() {
        motion.onResume();
        AppBusiness.INSTANCE.onResume(new PreferencesProvider(getApplicationContext()), motion);
        init_tab = AppBusiness.INSTANCE.getGuiStatus().selected_tab;
    }


    @Override
    public void exit() {
        // nop
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppBusiness.INSTANCE.onPause(new PreferencesProvider(getApplicationContext()));
        motion.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (AppBusiness.INSTANCE) {
            if (!AppBusiness.INSTANCE.is_resumed()) {
                resume_app_business();
            }
        }
        centralTabLayout.selectTab(centralTabLayout.getTabAt(init_tab));
    }
}