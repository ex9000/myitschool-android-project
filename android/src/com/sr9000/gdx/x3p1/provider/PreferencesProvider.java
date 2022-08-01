package com.sr9000.gdx.x3p1.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sr9000.gdx.x3p1.business.state.ICircleReader;
import com.sr9000.gdx.x3p1.business.state.ILog;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;
import com.sr9000.gdx.x3p1.business.state.IRatingChanged;
import com.sr9000.gdx.x3p1.business.state.IRecordReader;
import com.sr9000.gdx.x3p1.log.Logger;
import com.sr9000.gdx.x3p1.sqlite.CircleReaderDbHelper;
import com.sr9000.gdx.x3p1.sqlite.RecordReaderDbHelper;

public class PreferencesProvider implements IPreferencesProvider {
    SharedPreferences p;
    RecordReaderDbHelper recordReader;
    CircleReaderDbHelper circleReader;

    public PreferencesProvider(Context context) {
        p = PreferenceManager.getDefaultSharedPreferences(context);
        recordReader = new RecordReaderDbHelper(context);
        circleReader = new CircleReaderDbHelper(context);
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return p.getInt(name, defaultValue);
    }

    @Override
    public long getLong(String name, long defaultValue) {
        return p.getLong(name, defaultValue);
    }

    @Override
    public String getString(String name, String defaultValue) {
        return p.getString(name, defaultValue);
    }

    @Override
    public void putInt(String name, int value) {
        SharedPreferences.Editor e = p.edit();
        e.putInt(name, value);
        e.apply();
    }

    @Override
    public void putLong(String name, long value) {
        SharedPreferences.Editor e = p.edit();
        e.putLong(name, value);
        e.apply();
    }

    @Override
    public void putString(String name, String value) {
        SharedPreferences.Editor e = p.edit();
        e.putString(name, value);
        e.apply();
    }

    @Override
    public boolean getBool(String name, boolean defaultValue) {
        return p.getBoolean(name, defaultValue);
    }

    @Override
    public void putBool(String name, boolean value) {
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(name, value);
        e.apply();
    }

    @Override
    public ILog getLog() {
        return new Logger();
    }

    @Override
    public IRatingChanged getRatingChanged() {
        return RVChanger.INSTANCE;
    }

    @Override
    public IRecordReader getRecordReader() {
        return recordReader;
    }

    @Override
    public ICircleReader getCircleReader() {
        return circleReader;
    }

}
