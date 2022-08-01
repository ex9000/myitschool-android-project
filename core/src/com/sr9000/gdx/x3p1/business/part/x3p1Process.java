package com.sr9000.gdx.x3p1.business.part;


import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class x3p1Process extends ProtoPausable {

    String prefix;

    public boolean is_active;
    public long current_number;

    public long best_number;
    public long best_hops;

    RateMeasure rate;
    boolean keep_going;
    Thread t;

    public x3p1Process(String processName) {
        prefix = processName;
        rate = new RateMeasure(processName);
    }

    public long get_rps() {
        float tmean = rate.get_rate_ns();
        if (tmean == 0.0) {
            return 0;
        }

        return (long) (1.0e+9f * (1 / tmean));
    }

    @Override
    protected void sanitize() {
        is_active = false;
        current_number = 0;
        best_number = 0;
        best_hops = 0;
        rate.sanitize();
    }

    @Override
    protected void save_state(IPreferencesProvider p) {
        keep_going = false;
        try {
            t.join(1000);
        } catch (InterruptedException e) {
            // nop
        }

        rate.save_state(p);
        p.putBool(nameOf(prefix, "is_active"), is_active);
        p.putLong(nameOf(prefix, "current_number"), current_number);
        p.putLong(nameOf(prefix, "best_number"), best_number);
        p.putLong(nameOf(prefix, "best_hops"), best_hops);
    }

    @Override
    protected void load_state(IPreferencesProvider p) {
        rate.load_state(p);
        is_active = p.getBool(nameOf(prefix, "is_active"), is_active);
        current_number = p.getLong(nameOf(prefix, "current_number"), current_number);
        best_number = p.getLong(nameOf(prefix, "best_number"), best_number);
        best_hops = p.getLong(nameOf(prefix, "best_hops"), best_hops);


        keep_going = true;
        t = new Thread(() -> {
            try {
                long begin = System.nanoTime();

                while (keep_going) {
                    Thread.sleep(0);

                    if (!AppBusiness.INSTANCE.is_resumed() || !AppBusiness.INSTANCE.getSimulation().enabled || !is_active) {
                        continue;
                    }

                    //AppBusiness.INSTANCE.log.warning(String.format("Start %s %d", prefix, current_number));
                    if (current_number < 1) {
                        synchronized (AppBusiness.INSTANCE) {
                            if (AppBusiness.INSTANCE.is_resumed()) {
                                current_number = AppBusiness.INSTANCE.getSimulation().get_next_number();
                            }
                        }
                        continue;
                    }

                    long number = current_number;
                    long hops = 0;
                    while (number > 0 && number != 1) {
                        hops++;
                        if (number % 2 == 0) {
                            number /= 2;
                        } else {
                            number = 3 * number + 1;
                        }
                    }

                    if (number < 0) {
                        synchronized (AppBusiness.INSTANCE) {
                            current_number = AppBusiness.INSTANCE.getSimulation().get_next_number();
                        }
                        continue;
                    }

                    synchronized (AppBusiness.INSTANCE) {
                        if (AppBusiness.INSTANCE.is_resumed()) {
                            AppBusiness.INSTANCE.getPhysics().push_number(current_number, hops, prefix);
                            AppBusiness.INSTANCE.getSimulation().push_number(current_number, hops, prefix);

                            if (best_hops < hops) {
                                best_number = current_number;
                                best_hops = hops;
                            }

                            current_number = AppBusiness.INSTANCE.getSimulation().get_next_number();

                            long end = System.nanoTime();
                            rate.push_measure_ns(end - begin);
                            begin = end;
                        }
                    }
                }
            } catch (InterruptedException e) {
                // nop
            }
        });
        t.start();
    }

}
