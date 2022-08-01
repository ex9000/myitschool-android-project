package com.sr9000.gdx.x3p1.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sr9000.gdx.x3p1.R;
import com.sr9000.gdx.x3p1.impl.RecordRecyclerViewAdapter;
import com.sr9000.gdx.x3p1.provider.RVChanger;

public class CreditsTabFragment extends Fragment {

    public CreditsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credits_tab, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        RVChanger.INSTANCE.setRv(null, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        // set up the RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.top_numbers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecordRecyclerViewAdapter adapter = new RecordRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        RVChanger.INSTANCE.setRv(recyclerView, getActivity());
    }
}