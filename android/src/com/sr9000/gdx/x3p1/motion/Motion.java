package com.sr9000.gdx.x3p1.motion;

import android.content.Context;

import com.sr9000.gdx.x3p1.business.state.IMotionProvider;

public class Motion implements IMotionProvider {
    Context context;
    Accelerometer acc;
    Rotation rot;

    public Motion(Context context) {
        this.context = context;

        acc = new Accelerometer(context);
        rot = new Rotation(context);
    }

    public void onPause() {
        acc.onPause();
        rot.onPause();
    }

    public void onResume() {
        acc.onResume();
        rot.onResume();
    }

    @Override
    public float[] get_acceleration() {
        switch (ScreenRotation.getScreenRotation(context)) {
            case SCREEN_ORIENTATION_TOP:
                return new float[]{acc.x, acc.y, acc.z};
            case SCREEN_ORIENTATION_BOTTOM:
                return new float[]{-acc.x, -acc.y, acc.z};
            case SCREEN_ORIENTATION_RIGHT:
                return new float[]{-acc.y, acc.x, acc.z};
            case SCREEN_ORIENTATION_LEFT:
                return new float[]{acc.y, -acc.x, acc.z};
        }

        return new float[]{acc.x, acc.y, acc.z};
    }

    @Override
    public float[] get_angular() {
        switch (ScreenRotation.getScreenRotation(context)) {
            case SCREEN_ORIENTATION_TOP:
                return new float[]{rot.wx, rot.wy, rot.wz};
            case SCREEN_ORIENTATION_BOTTOM:
                return new float[]{-rot.wx, -rot.wy, rot.wz};
            case SCREEN_ORIENTATION_RIGHT:
                return new float[]{-rot.wy, rot.wx, rot.wz};
            case SCREEN_ORIENTATION_LEFT:
                return new float[]{rot.wy, -rot.wx, rot.wz};
        }

        return new float[]{rot.wx, rot.wy, rot.wz};
    }
}
