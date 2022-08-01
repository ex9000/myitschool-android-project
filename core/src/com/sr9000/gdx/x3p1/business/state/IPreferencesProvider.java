package com.sr9000.gdx.x3p1.business.state;

public interface IPreferencesProvider {

    boolean getBool(String name, boolean defaultValue);

    int getInt(String name, int defaultValue);

    long getLong(String name, long defaultValue);

    String getString(String name, String defaultValue);

    void putBool(String name, boolean value);

    void putInt(String name, int value);

    void putLong(String name, long value);

    void putString(String name, String value);

    ILog getLog();

    IRatingChanged getRatingChanged();

    IRecordReader getRecordReader();

    ICircleReader getCircleReader();

}
