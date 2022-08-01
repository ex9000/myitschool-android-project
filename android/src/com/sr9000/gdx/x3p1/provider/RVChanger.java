package com.sr9000.gdx.x3p1.provider;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.sr9000.gdx.x3p1.business.state.IRatingChanged;

public class RVChanger implements IRatingChanged {
    public static final RVChanger INSTANCE = new RVChanger();
    RecyclerView rv;
    Activity ac;

    private RVChanger() {
        rv = null;
    }

    @Override
    public void push() {
        synchronized (this) {
            if (rv != null) {
                RecyclerView.Adapter adapter = rv.getAdapter();
                if (adapter != null) {
                    ac.runOnUiThread(()->{
                        adapter.notifyDataSetChanged();
                    });
                }
            }
        }
    }

    public void setRv(RecyclerView recyclerView, Activity activity) {
        synchronized (this) {
            rv = recyclerView;
            ac = activity;
        }
    }
}
