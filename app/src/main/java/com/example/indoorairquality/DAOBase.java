package com.example.indoorairquality;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
public abstract class DAOBase {
    // This abstract class is used to handle the database in order to avoid plenty of code in
    // the class DataBase
    //Value to change if we change the database
    protected final static int VERSION = 3;
    // Name of the file representing my base
    protected final static String NOM = "database.db";

    protected SQLiteDatabase mDb = null;
    protected DataBase mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new DataBase(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
