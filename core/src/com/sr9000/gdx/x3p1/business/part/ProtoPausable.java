package com.sr9000.gdx.x3p1.business.part;

import com.sr9000.gdx.x3p1.business.state.IMotionProvider;
import com.sr9000.gdx.x3p1.business.state.IPausable;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;

import java.util.Arrays;

abstract class ProtoPausable implements IPausable {

    protected abstract void sanitize();

    protected abstract void save_state(IPreferencesProvider p);

    protected abstract void load_state(IPreferencesProvider p);

    protected String nameOf(String property) {
        return this.getClass().getCanonicalName() + "." + property;
    }

    protected String nameOf(String... property) {
        assert property.length > 0;

        String full_property = Arrays.stream(property).reduce((s, s2) -> s + "." + s2).get();
        return this.getClass().getCanonicalName() + "." + full_property;
    }

    @Override
    public void onPause(IPreferencesProvider p) {
        save_state(p);
        sanitize();
    }

    @Override
    public void onResume(IPreferencesProvider p, IMotionProvider m) {
        sanitize();
        load_state(p);
    }

    @Override
    public void reset(IPreferencesProvider p) {
        save_state(p);
        sanitize();
        save_state(p);
        load_state(p);
    }
}
