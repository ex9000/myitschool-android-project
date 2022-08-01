package com.sr9000.gdx.x3p1.business.draw;

public class DrawHelper {
    float x0, y0, f;

    public void init(int width, int height, float scale) {
        if (width > height) {
            y0 = 0;
            x0 = 0.5f * (width - height);
            f = height / scale;
        } else {
            x0 = 0;
            y0 = 0.5f * (height - width);
            f = width / scale;
        }
    }

    public float x(float x) {
        return x0 + f * x;
    }

    public float y(float y) {
        return y0 + f * y;
    }

    public float s(float s) {
        return f * s;
    }
}
