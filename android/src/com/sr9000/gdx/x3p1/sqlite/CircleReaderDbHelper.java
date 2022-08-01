package com.sr9000.gdx.x3p1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sr9000.gdx.x3p1.business.part.struct.x3p1Circle;
import com.sr9000.gdx.x3p1.business.state.ICircleReader;
import com.sr9000.gdx.x3p1.sqlite.contract.CircleReaderContract.CircleEntry;

import java.util.ArrayList;

public class CircleReaderDbHelper extends SQLiteOpenHelper implements ICircleReader {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CircleEntry.TABLE_NAME + " (" +
                    CircleEntry._ID + " INTEGER PRIMARY KEY," +
                    CircleEntry.COLUMN_NAME_NUMBER + " INTEGER," +
                    CircleEntry.COLUMN_NAME_HOPS + " INTEGER," +
                    CircleEntry.COLUMN_NAME_COLOR + " INTEGER," +
                    CircleEntry.COLUMN_NAME_X + " REAL," +
                    CircleEntry.COLUMN_NAME_Y + " REAL," +
                    CircleEntry.COLUMN_NAME_R + " REAL)";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + CircleEntry.TABLE_NAME;

    private static final String SQL_DELETE_ALL_ROWS =
            "DELETE FROM " + CircleEntry.TABLE_NAME;

//    private static final String SQL_INSERT_ENTRY =
//            "INSERT INTO " + RecordEntry.TABLE_NAME + "(" +
//            RecordEntry.COLUMN_NAME_NUMBER + "," +
//            RecordEntry.COLUMN_NAME_HOPS + "," +
//            RecordEntry.COLUMN_NAME_AUTHOR + "," +
//            RecordEntry.COLUMN_NAME_TIMESTAMP + ")" +
//            "VALUES(?,?,?,?)";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CircleReader.db";

    public CircleReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no upgrade policy
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no downgrade policy
        onUpgrade(db, oldVersion, newVersion);
    }


    public void save_circles(x3p1Circle[] circles) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ALL_ROWS);

        db.beginTransaction();

        for (x3p1Circle c : circles) {
//            SQLiteStatement insert = db.compileStatement(SQL_INSERT_ENTRY);
//
//            insert.bindLong(1, r.number);
//            insert.bindLong(2, r.hops);
//            insert.bindString(3, r.author);
//            insert.bindString(4, r.datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            insert.execute();

            ContentValues values = new ContentValues();
            values.put(CircleEntry.COLUMN_NAME_NUMBER, c.number);
            values.put(CircleEntry.COLUMN_NAME_HOPS, c.hops);
            values.put(CircleEntry.COLUMN_NAME_COLOR, c.color);
            values.put(CircleEntry.COLUMN_NAME_X, c.x);
            values.put(CircleEntry.COLUMN_NAME_Y, c.y);
            values.put(CircleEntry.COLUMN_NAME_R, c.original_r);

            db.insert(CircleEntry.TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public ArrayList<x3p1Circle> load_circles() {

        ArrayList<x3p1Circle> circles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CircleEntry.COLUMN_NAME_NUMBER,
                CircleEntry.COLUMN_NAME_HOPS,
                CircleEntry.COLUMN_NAME_COLOR,
                CircleEntry.COLUMN_NAME_X,
                CircleEntry.COLUMN_NAME_Y,
                CircleEntry.COLUMN_NAME_R
        };

        Cursor cursor = db.query(CircleEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            x3p1Circle c = new x3p1Circle();
            c.number = cursor.getLong(cursor.getColumnIndexOrThrow(CircleEntry.COLUMN_NAME_NUMBER));
            c.hops = cursor.getLong(cursor.getColumnIndexOrThrow(CircleEntry.COLUMN_NAME_HOPS));
            c.color = cursor.getInt(cursor.getColumnIndexOrThrow(CircleEntry.COLUMN_NAME_COLOR));
            c.x = cursor.getFloat(cursor.getColumnIndexOrThrow(CircleEntry.COLUMN_NAME_X));
            c.y = cursor.getFloat(cursor.getColumnIndexOrThrow(CircleEntry.COLUMN_NAME_Y));
            c.original_r = cursor.getFloat(cursor.getColumnIndexOrThrow(CircleEntry.COLUMN_NAME_R));
            c.real_r = c.original_r;

            circles.add(c);
        }
        cursor.close();

        db.close();

        return circles;
    }

}