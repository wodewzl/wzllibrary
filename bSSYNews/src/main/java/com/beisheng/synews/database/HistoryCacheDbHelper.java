
package com.beisheng.synews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryCacheDbHelper extends SQLiteOpenHelper {
    public HistoryCacheDbHelper(Context context, int version) {
        super(context, "webCache1.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("create table if not exists Cache (id INTEGER primary key autoincrement,urlId INTEGER unique,json text)");
        db.execSQL("create table  if not exists  jsonCache( id INTEGER primary key autoincrement,name text,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
