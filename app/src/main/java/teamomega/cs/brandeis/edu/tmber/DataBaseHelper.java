package teamomega.cs.brandeis.edu.tmber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Edwin on 11/16/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Buddy.db";
    public static final String TABLE_NAME = "Buddy_info";
    public static final String COL_1 = "_ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "NUM";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME  +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, NUM TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertData(String name, String num) {

        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        values.put(COL_3, num);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insert(TABLE_NAME, null, values);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        return res;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id = ?", new String[] {id});
    }
}