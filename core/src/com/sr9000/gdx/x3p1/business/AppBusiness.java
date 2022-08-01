package com.sr9000.gdx.x3p1.business;

import com.sr9000.gdx.x3p1.business.part.GUIStatus;
import com.sr9000.gdx.x3p1.business.state.ILog;
import com.sr9000.gdx.x3p1.business.state.IMotionProvider;
import com.sr9000.gdx.x3p1.business.state.IPausable;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;
import com.sr9000.gdx.x3p1.business.part.Physics;
import com.sr9000.gdx.x3p1.business.part.Simulation;
import com.sr9000.gdx.x3p1.business.part.x3p1Process;


public class AppBusiness implements IPausable {

    public ILog log;

    GUIStatus guiStatus = new GUIStatus();
    Simulation simulation = new Simulation();
    x3p1Process orange = new x3p1Process("orange");
    x3p1Process blue = new x3p1Process("blue");
    Physics physics = new Physics();

    IMotionProvider motionProvider;

    IPausable[] parts;

    boolean resumed;

    public static final AppBusiness INSTANCE = new AppBusiness();

    private AppBusiness() {
        parts = new IPausable[]{orange, blue, physics, simulation, guiStatus};
        resumed = false;
    }

    public GUIStatus getGuiStatus() {
        return guiStatus;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public x3p1Process getOrange() {
        return orange;
    }

    public x3p1Process getBlue() {
        return blue;
    }

    public Physics getPhysics() {
        return physics;
    }

    public IMotionProvider getMotionProvider() {
        return motionProvider;
    }

    public boolean is_resumed() {
        return resumed;
    }

    @Override
    public void onPause(IPreferencesProvider p) {
        resumed = false;
        for (IPausable x : parts) {
            x.onPause(p);
        }
    }

    @Override
    public void onResume(IPreferencesProvider p, IMotionProvider m) {
        motionProvider = m;
        log = p.getLog();

        for (IPausable x : parts) {
            x.onResume(p, m);
        }
        resumed = true;
    }

    public void reset(IPreferencesProvider p) {
        for (IPausable x : parts) {
            x.reset(p);
        }
    }
}
