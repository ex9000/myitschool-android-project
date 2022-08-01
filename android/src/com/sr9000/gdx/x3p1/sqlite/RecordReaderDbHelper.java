package com.sr9000.gdx.x3p1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sr9000.gdx.x3p1.business.part.struct.Record;
import com.sr9000.gdx.x3p1.business.state.IRecordReader;
import com.sr9000.gdx.x3p1.sqlite.contract.RecordReaderContract.RecordEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecordReaderDbHelper extends SQLiteOpenHelper implements IRecordReader {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + RecordEntry.TABLE_NAME + " (" +
                    RecordEntry._ID + " INTEGER PRIMARY KEY," +
                    RecordEntry.COLUMN_NAME_NUMBER + " INTEGER," +
                    RecordEntry.COLUMN_NAME_HOPS + " INTEGER," +
                    RecordEntry.COLUMN_NAME_AUTHOR + " TEXT," +
                    RecordEntry.COLUMN_NAME_TIMESTAMP + " TEXT)";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;

    private static final String SQL_DELETE_ALL_ROWS =
            "DELETE FROM " + RecordEntry.TABLE_NAME;

//    private static final String SQL_INSERT_ENTRY =
//            "INSERT INTO " + RecordEntry.TABLE_NAME + "(" +
//            RecordEntry.COLUMN_NAME_NUMBER + "," +
//            RecordEntry.COLUMN_NAME_HOPS + "," +
//            RecordEntry.COLUMN_NAME_AUTHOR + "," +
//            RecordEntry.COLUMN_NAME_TIMESTAMP + ")" +
//            "VALUES(?,?,?,?)";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RecordReader.db";

    public RecordReaderDbHelper(Context context) {
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

    public void save_records(Record[] records) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ALL_ROWS);

        db.beginTransaction();

        for (Record r : records) {
//            SQLiteStatement insert = db.compileStatement(SQL_INSERT_ENTRY);
//
//            insert.bindLong(1, r.number);
//            insert.bindLong(2, r.hops);
//            insert.bindString(3, r.author);
//            insert.bindString(4, r.datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            insert.execute();

            ContentValues values = new ContentValues();
            values.put(RecordEntry.COLUMN_NAME_NUMBER, r.number);
            values.put(RecordEntry.COLUMN_NAME_HOPS, r.hops);
            values.put(RecordEntry.COLUMN_NAME_AUTHOR, r.author);
            values.put(RecordEntry.COLUMN_NAME_TIMESTAMP, r.datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            db.insert(RecordEntry.TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public ArrayList<Record> load_records() {
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                RecordEntry.COLUMN_NAME_NUMBER,
                RecordEntry.COLUMN_NAME_HOPS,
                RecordEntry.COLUMN_NAME_AUTHOR,
                RecordEntry.COLUMN_NAME_TIMESTAMP
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = RecordEntry.COLUMN_NAME_HOPS + " DESC";

        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
        while (cursor.moveToNext()) {
            Record r = new Record();
            r.number = cursor.getLong(cursor.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_NUMBER));
            r.hops = cursor.getLong(cursor.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_HOPS));
            r.author = cursor.getString(cursor.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_AUTHOR));

            String tstmp = cursor.getString(cursor.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_TIMESTAMP));
            r.datetime = LocalDateTime.parse(tstmp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            records.add(r);
        }
        cursor.close();

        db.close();

        return records;
    }

}