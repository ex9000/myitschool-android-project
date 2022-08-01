package com.sr9000.gdx.x3p1.business.part;

import com.google.gson.Gson;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;

public class RateMeasure extends ProtoPausable {

    final static int N_MEASURES = 100;

    String prefix;
    long[] evaluated_ns = new long[N_MEASURES];
    long evaluated_sum;
    int ilast;

    public RateMeasure(String processName) {
        prefix = processName;
    }

    public void push_measure_ns(long t_ns) {
        if (ilast < 0) {
            for (int i = 0; i < N_MEASURES; i++) {
                evaluated_ns[i] = t_ns;
            }
            evaluated_sum = N_MEASURES * t_ns;
            ilast = 0;
        }

        evaluated_sum -= evaluated_ns[ilast];
        evaluated_sum += t_ns;
        evaluated_ns[ilast] = t_ns;
        ilast = (ilast + 1) % N_MEASURES;
    }

    public float get_rate_ns() {
        return ((float) evaluated_sum) / ((float) N_MEASURES);
    }

    @Override
    protected void sanitize() {
        evaluated_ns = new long[N_MEASURES];
        evaluated_sum = 0;
        ilast = -1;
    }

    @Override
    protected void save_state(IPreferencesProvider p) {
        p.putLong(nameOf(prefix, "evaluated_sum"), evaluated_sum);
        p.putInt(nameOf(prefix, "ilast"), ilast);

        Gson gson = new Gson();
        String tmp = gson.toJson(evaluated_ns);
        p.putString(nameOf(prefix, "evaluated_ns"), tmp);
    }

    @Override
    protected void load_state(IPreferencesProvider p) {
        evaluated_sum = p.getLong(nameOf(prefix, "evaluated_sum"), evaluated_sum);
        ilast = p.getInt(nameOf(prefix, "ilast"), ilast);

        Gson gson = new Gson();
        String defaultTmp = gson.toJson(evaluated_ns);
        String tmp = p.getString(nameOf(prefix, "evaluated_ns"), defaultTmp);
        evaluated_ns = gson.fromJson(tmp, long[].class);
    }

}
