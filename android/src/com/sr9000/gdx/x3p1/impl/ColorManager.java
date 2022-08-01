package com.sr9000.gdx.x3p1.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;

import com.sr9000.gdx.x3p1.BasicColor;

public class ColorManager {
    float r, g, b;

    public ColorManager(Activity activity) {
        TypedValue a = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);

        int color = a.data;
        r = Color.red(color) / 255.0f;
        g = Color.green(color) / 255.0f;
        b = Color.blue(color) / 255.0f;
    }

    public BasicColor toBasicColor() {
        return new BasicColor(r, g, b);
    }
}
