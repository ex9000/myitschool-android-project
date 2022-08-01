package com.sr9000.gdx.x3p1.log;

import android.util.Log;

import com.sr9000.gdx.x3p1.business.state.ILog;

public class Logger implements ILog {
    @Override
    public void warning(String msg) {
        Log.w("Wubbalubbadubdub", msg);
    }

    @Override
    public void error(String msg) {
        Log.wtf("Wubbalubbadubdub", msg);
    }
}
