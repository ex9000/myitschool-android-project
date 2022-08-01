package com.sr9000.gdx.x3p1.motion;

import android.content.Context;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public enum ScreenRotation {
    SCREEN_ORIENTATION_TOP,
    SCREEN_ORIENTATION_BOTTOM,
    SCREEN_ORIENTATION_RIGHT,
    SCREEN_ORIENTATION_LEFT;

    public static ScreenRotation getScreenRotation(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return SCREEN_ORIENTATION_TOP;

            case Surface.ROTATION_90:
                return SCREEN_ORIENTATION_RIGHT;

            case Surface.ROTATION_180:
                return SCREEN_ORIENTATION_BOTTOM;

            case Surface.ROTATION_270:
                return SCREEN_ORIENTATION_LEFT;
        }

        return SCREEN_ORIENTATION_TOP;
    }
}
