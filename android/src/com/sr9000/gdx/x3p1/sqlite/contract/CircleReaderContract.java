package com.sr9000.gdx.x3p1.sqlite.contract;

import android.provider.BaseColumns;

public final class CircleReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CircleReaderContract() {}

    /* Inner class that defines the table contents */
    public static class CircleEntry implements BaseColumns {
        public static final String TABLE_NAME = "circle";
        public static final String COLUMN_NAME_X = "xPos";
        public static final String COLUMN_NAME_Y = "yPos";
        public static final String COLUMN_NAME_R = "radius";
        public static final String COLUMN_NAME_NUMBER = "aNumber";
        public static final String COLUMN_NAME_HOPS = "hops";
        public static final String COLUMN_NAME_COLOR = "color";
    }
}
