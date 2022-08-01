package com.sr9000.gdx.x3p1.business.state;


public interface IPausable {

    void onPause(IPreferencesProvider p);

    void onResume(IPreferencesProvider p, IMotionProvider m);

    void reset(IPreferencesProvider p);

}
