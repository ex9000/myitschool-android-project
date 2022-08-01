package com.sr9000.gdx.x3p1.sqlite.contract;

import android.provider.BaseColumns;

public class RecordReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RecordReaderContract() {}

    /* Inner class that defines the table contents */
    public static class RecordEntry implements BaseColumns {
        public static final String TABLE_NAME = "hopsRecord";
        public static final String COLUMN_NAME_NUMBER = "aNumber";
        public static final String COLUMN_NAME_HOPS = "hops";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TIMESTAMP = "aTimestamp";
    }
}
