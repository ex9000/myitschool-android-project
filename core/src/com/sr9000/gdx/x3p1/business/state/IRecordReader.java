package com.sr9000.gdx.x3p1.business.state;

import com.sr9000.gdx.x3p1.business.part.struct.Record;

import java.util.ArrayList;

public interface IRecordReader {

    void save_records(Record[] records);

    ArrayList<Record> load_records();

}
