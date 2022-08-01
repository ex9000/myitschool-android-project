package com.sr9000.gdx.x3p1.business.part;

import com.sr9000.gdx.x3p1.business.part.struct.Record;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;
import com.sr9000.gdx.x3p1.business.state.IRatingChanged;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Simulation extends ProtoPausable {

    final static int N_RECORDS = 100;

    public long next_number;
    public boolean enabled;

    ArrayList<Record> records;
    IRatingChanged ratingChanged;


    @Override
    protected void sanitize() {
        ratingChanged = () -> {
        };
        next_number = 1;
        enabled = false;
        records = new ArrayList<>();
    }

    public long get_next_number() {
        if (next_number < 1) {
            next_number = 1;
        }

        long r = next_number;
        next_number++;
        return r;
    }

    public Record[] get_records() {
        int size = records.size();
        Record[] arr = new Record[size];

        int i = 0;
        for (Record r : records) {
            arr[i] = new Record(r);
            i++;
        }

        return arr;
    }

    public int get_size() {
        return records.size();
    }

    public Record get_by_position(int pos) {
        return new Record(records.get(pos));
    }

    public void push_number(long number, long hops, String author) {
        if (records.isEmpty()) {
            records.add(new Record(number, hops, author));
            ratingChanged.push();
            return;
        }

        if (records.get(0).hops < hops) {
            if (records.size() >= N_RECORDS) {
                records.remove(records.size() - 1);
            }

            records.add(0, new Record(number, hops, author));
            ratingChanged.push();
        }
    }

    @Override
    protected void save_state(IPreferencesProvider p) {
        p.putLong(nameOf("next_number"), next_number);
        p.putBool(nameOf("enabled"), enabled);

        Record[] arr = new Record[records.size()];
        records.toArray(arr);
        p.getRecordReader().save_records(arr);
    }

    @Override
    protected void load_state(IPreferencesProvider p) {
        ratingChanged = p.getRatingChanged();
        next_number = p.getLong(nameOf("next_number"), next_number);
        enabled = p.getBool(nameOf("enabled"), enabled);

        records.addAll(p.getRecordReader().load_records());
    }

}
