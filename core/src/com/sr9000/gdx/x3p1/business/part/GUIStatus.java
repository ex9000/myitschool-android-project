package com.sr9000.gdx.x3p1.business.part;

import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;

public class GUIStatus extends ProtoPausable {

    public int selected_tab;

    @Override
    protected void sanitize() {
        selected_tab = 0;
    }

    @Override
    protected void save_state(IPreferencesProvider p) {
        p.putInt(nameOf("selected_tab"), selected_tab);
    }

    @Override
    protected void load_state(IPreferencesProvider p) {
        selected_tab = p.getInt(nameOf("selected_tab"), selected_tab);
    }

}
