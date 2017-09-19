
package com.beisheng.base.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作数据库辅助类
 * 
 * @author 2012-8-12
 */
public class CacheDbHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "sy_cache.db";
    public static final String CACHE_TABLE_NAME = "cache_table";
    public static final String KEYNAME = "keyname";
    public static final String JSONTEXT = "json";
    public static final String ISSAVED = "issaved";
    public static final String[] USER_COLS = {
            "keyname", "json",
    };

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public CacheDbHelper(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
        establishDb();
    }

    /**
     * 打开数据库
     */
    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    /**
     * 插入一条记录
     * 
     * @param map 要插入的记录
     * @param tableName 表名
     * @return 插入记录的id -1表示插入不成功
     */
    public void insertOrUpdate(String keyname, String json) {
        establishDb();

        boolean isUpdate = false;
        String[] keynames = queryAllKeyName();
        for (int i = 0; i < keynames.length; i++) {
            if (keyname.equals(keynames[i])) {
                isUpdate = true;
                break;
            }
        }
        long id = -1;
        if (isUpdate) {
            id = update(keyname, json);
        } else {
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put(KEYNAME, keyname);
                values.put(JSONTEXT, json);
                id = db.insert(CACHE_TABLE_NAME, null, values);
            }
        }
        update(keyname, json);
    }

    /**
     * 更新一条记录
     * 
     * @param
     * @param tableName 表名
     * @return 更新过后记录的id -1表示更新不成功
     */
    public long update(String keyname, String json) {
        establishDb();
        ContentValues values = new ContentValues();
        values.put(KEYNAME, keyname);
        values.put(JSONTEXT, json);
        long id = db.update(CACHE_TABLE_NAME, values, KEYNAME + " = '"
                + keyname + "'", null);
        return id;
    }

    /**
     * 根据用户名查询密码
     * 
     * @param username 用户名
     * @param tableName 表名
     * @return Hashmap 查询的记录
     */
    public String queryJsonByKeyName(String keyname) {
        establishDb();
        String sql = "select * from " + CACHE_TABLE_NAME + " where "
                + KEYNAME + " = '" + keyname + "'";
        Cursor cursor = db.rawQuery(sql, null);
        String json = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            json = cursor.getString(cursor.getColumnIndex(JSONTEXT));
        }
        return json;
    }

    /**
     * 关闭数据库
     */
    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    /**
     * 数据库辅助类
     */
    private static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + CACHE_TABLE_NAME + " (id INTEGER primary key autoincrement," + KEYNAME + " text, " + JSONTEXT + " text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CACHE_TABLE_NAME);
            onCreate(db);
        }

    }

    public String[] queryAllKeyName() {
        establishDb();
        if (db != null) {
            Cursor cursor = db.query(CACHE_TABLE_NAME, null, null, null, null,
                    null, null);
            int count = cursor.getCount();
            String[] keyNames = new String[count];
            if (count > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    keyNames[i] = cursor.getString(cursor
                            .getColumnIndex(KEYNAME));
                    cursor.moveToNext();
                }
            }
            return keyNames;
        } else {
            return new String[0];
        }

    }

    public List<String> queryLike(String likeKey) {
        List<String> list = new ArrayList<String>();
        String sql = "select * from " + CACHE_TABLE_NAME + " where keyname like ?";
        String[] selectionArgs = new String[] {
                "%" + likeKey + "%"
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex("json"));
            list.add(json);
        }
        return list;
    }

    public void deleteLike(String likeKey) {
        List<String> list = new ArrayList<String>();
        String sql = "delete  from " + CACHE_TABLE_NAME + " where keyname like ?";
        String[] selectionArgs = new String[] {
                "%" + likeKey + "%"
        };
        db.execSQL(sql, selectionArgs);
    }

    /**
     * 删除一条记录
     * 
     * @param userName 用户名
     * @param tableName 表名
     * @return 删除记录的id -1表示删除不成功
     */
    public void delete(String key) {
        establishDb();
        // db.delete(CACHE_TABLE_NAME, KEYNAME + " = ?", new String[] {
        // key
        // });

        // db.delete(CACHE_TABLE_NAME, KEYNAME + "=?", new String[] {
        // "1"
        // });
        String sql = "delete  from " + CACHE_TABLE_NAME + " where "
                + KEYNAME + " = '" + key + "'";
        db.execSQL(sql);
    }

    public void deleteJsonByKeyName(String keyname) {
        establishDb();
        String sql = "delete  from " + CACHE_TABLE_NAME + " where "
                + KEYNAME + " = '" + keyname + "'";
        db.execSQL(sql);
    }

}
