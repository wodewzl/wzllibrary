
package com.bs.bsims.chatutils;

import com.bs.bsims.application.BSApplication;
import com.yzxtcp.tools.CustomLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

 
public abstract class AbsDBManager {

    public static final String TAG = AbsDBManager.class.getSimpleName();

    private static SQLiteDatabase BsDb;
    private static BsDbHelper BsDbHelper;

    public AbsDBManager(Context context) {
        openDb(context, BSApplication.getInstance().getVersionCode());
    }

    private void openDb(Context context, int versionCode) {
        if (BsDbHelper == null) {
            BsDbHelper = new BsDbHelper(context, this, versionCode);
        }
        if (BsDb == null) {
            BsDb = BsDbHelper.getWritableDatabase();
        }
    }

    private void open(boolean isReadOnly) {
        if (BsDb == null) {
            if (isReadOnly) {
                BsDb = BsDbHelper.getReadableDatabase();
            } else {
                BsDb = BsDbHelper.getWritableDatabase();
            }
        }
    }

    public void destroy() {
        try {
            if (BsDbHelper != null) {
                BsDbHelper.close();
            }
            closeDb();
        } catch (Exception e) {
            CustomLog.d(e.toString());
        }
    }

    public final void reopen() {
        closeDb();
        open(false);
        CustomLog.d("----reopen this db----");
    }

    private void closeDb() {
        if (BsDb != null) {
            BsDb.close();
            BsDb = null;
        }
    }

    protected final SQLiteDatabase sqliteDB() {
        open(false);
        return BsDb;
    }

    public static class BsDbHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "bschat.db";
        public static final String TABLE_CONVERSATION_BG = "conversation_bg";

        private AbsDBManager dbManager;

        public BsDbHelper(Context context, AbsDBManager dbManager, int version) {
            this(context, DATABASE_NAME, null, version, dbManager);
        }

        public BsDbHelper(Context context, String name, CursorFactory factory,
                int version, AbsDBManager dbManager) {
            super(context, name, factory, version);
            this.dbManager = dbManager;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           
        }

        public void createTables(SQLiteDatabase db) {
            createConversationBgTable(db);
        }

        private void createConversationBgTable(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_CONVERSATION_BG
                    + " ("
                    + ConversationBgColume._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ConversationBgColume._IMUSERID + " TEXT, "
                    + ConversationBgColume._TARGETID + " TEXT, "
                    + ConversationBgColume._BGPATH + " TEXT"
                    + ")";
            db.execSQL(sql);
        }

        public static class BaseColumn {
            public static final String _ID = "_id";
        }

        public static class ConversationBgColume extends BaseColumn {
            public static final String _IMUSERID = "_imuserid";
            public static final String _TARGETID = "_targetId";
            public static final String _BGPATH = "_bgPath";
        }
    }

}
