package com.sr9000.gdx.x3p1.tabs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.sr9000.gdx.x3p1.R;
import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.log.Logger;
import com.sr9000.gdx.x3p1.provider.PreferencesProvider;

import java.util.concurrent.Callable;

public class SettingsTabFragment extends Fragment {

    final static TextWatcher watcher_e_num = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try {
                AppBusiness.INSTANCE.getSimulation().next_number = Long.parseLong(editable.toString());
            } catch (NumberFormatException e) {
                // nop
            }
        }
    };

    SwitchCompat sw_blue, sw_orange, sw_sim;
    TextView txt_orange_rate, txt_orange_best, txt_blue_rate, txt_blue_best;
    EditText e_num;
    Button bt_reset;

    boolean keep_going;
    Thread t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void find_by_id() {
        sw_blue = requireActivity().findViewById(R.id.switch_blue);
        sw_orange = requireActivity().findViewById(R.id.switch_orange);
        sw_sim = requireActivity().findViewById(R.id.switch_sim);

        txt_orange_rate = requireActivity().findViewById(R.id.text_orange_rate);
        txt_orange_best = requireActivity().findViewById(R.id.text_orange_best);
        txt_blue_rate = requireActivity().findViewById(R.id.text_blue_rate);
        txt_blue_best = requireActivity().findViewById(R.id.text_blue_best);

        e_num = requireActivity().findViewById(R.id.edit_text_current);
        bt_reset = requireActivity().findViewById(R.id.button_reset);
    }

    private void set_observers() {
        sw_blue.setOnCheckedChangeListener((compoundButton, b) -> AppBusiness.INSTANCE.getBlue().is_active = b);
        sw_orange.setOnCheckedChangeListener((compoundButton, b) -> AppBusiness.INSTANCE.getOrange().is_active = b);
        sw_sim.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppBusiness.INSTANCE.getOrange().current_number = AppBusiness.INSTANCE.getSimulation().get_next_number();
                AppBusiness.INSTANCE.getBlue().current_number = AppBusiness.INSTANCE.getSimulation().get_next_number();
            }
            AppBusiness.INSTANCE.getSimulation().enabled = b;
            sync_e_num_activity();
        });

        bt_reset.setOnClickListener(view -> {
            AppBusiness.INSTANCE.reset(new PreferencesProvider(getContext()));
            init_from_business();
        });
    }

    private void sync_e_num_activity() {
        long next_number = AppBusiness.INSTANCE.getSimulation().next_number;
        if (AppBusiness.INSTANCE.getSimulation().enabled) {
            e_num.setFocusable(false);
            e_num.setEnabled(false);
            e_num.removeTextChangedListener(watcher_e_num);
        } else {
            e_num.setEnabled(true);
            e_num.setFocusableInTouchMode(true);
            e_num.addTextChangedListener(watcher_e_num);
        }
        e_num.setText(String.valueOf(next_number));
    }

    private void init_from_business() {
        sw_blue.setChecked(AppBusiness.INSTANCE.getBlue().is_active);
        sw_orange.setChecked(AppBusiness.INSTANCE.getOrange().is_active);
        sw_sim.setChecked(AppBusiness.INSTANCE.getSimulation().enabled);

        sync_e_num_activity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_tab, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        keep_going = false;
        try {
            t.join(1000);
        } catch (InterruptedException e) {
            // nop
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        find_by_id();
        init_from_business();
        set_observers();

        keep_going = true;
        t = new Thread(() -> {
            while (keep_going) {
                try {
                    Thread.sleep(100);

                    synchronized (AppBusiness.INSTANCE) {
                        if (!AppBusiness.INSTANCE.is_resumed()) {
                            continue;
                        }

                        getActivity().runOnUiThread(() -> {
                            txt_orange_best.setText(
                                    String.format("Best: %,d (%d hops)", AppBusiness.INSTANCE.getOrange().best_number, AppBusiness.INSTANCE.getOrange().best_hops)
                            );
                            txt_blue_best.setText(
                                    String.format("Best: %,d (%d hops)", AppBusiness.INSTANCE.getBlue().best_number, AppBusiness.INSTANCE.getBlue().best_hops)
                            );

                            txt_orange_rate.setText(
                                    String.format("Rate: %s", magnitude_format(AppBusiness.INSTANCE.getOrange().get_rps()))
                            );
                            txt_blue_rate.setText(
                                    String.format("Rate: %s", magnitude_format(AppBusiness.INSTANCE.getBlue().get_rps()))
                            );

                            if (AppBusiness.INSTANCE.getSimulation().enabled) {
                                e_num.setText(String.valueOf(AppBusiness.INSTANCE.getSimulation().next_number));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    // nop
                }
            }
        });
        t.start();
    }

    private String magnitude_format(long rps) {
        // K M B
        if (rps > 1e+9) {
            return String.format("%.1fB", rps / 1.0e+9f);
        } else if (rps > 1e+6) {
            return String.format("%.1fM", rps / 1.0e+6f);
        } else if (rps > 1e+3) {
            return String.format("%.1fK", rps / 1.0e+3f);
        } else {
            return String.format("%d", rps);
        }
    }

}