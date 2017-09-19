
package com.bs.bsims.chatutils;

import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.AbsDBManager.BsDbHelper.ConversationBgColume;

public class ConversationBgDbManager extends AbsDBManager implements IDBManager<ConversationBg> {

    private static final String TAG = ConversationBgDbManager.class.getSimpleName();

    private static ConversationBgDbManager instance;

    public static ConversationBgDbManager getInstance() {
        if (instance == null) {
            instance = new ConversationBgDbManager(BSApplication.getInstance());
        }
        return instance;
    }

    public ConversationBgDbManager(Context context) {
        super(context);
    }

    @Override
    public List<ConversationBg> getAll() {
        return null;
    }

    public ConversationBg getById(String targetId) {
        String account = BSApplication.getInstance().getIMjavaBean().getClient().get(0).getUserId();
        String sql = null;
        Cursor cursor = null;
        ConversationBg conversationBg = null;
        try {
            sql = "select * from " + BsDbHelper.TABLE_CONVERSATION_BG
                    + " where _imuserid = " + account + " and _targetId = " + targetId;
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            while (cursor.moveToFirst()) {
                conversationBg = new ConversationBg();
                conversationBg.setId(cursor.getString(cursor.getColumnIndex(ConversationBgColume._ID)));
                conversationBg.setAccount(cursor.getString(cursor.getColumnIndex(ConversationBgColume._IMUSERID)));
                conversationBg.setTargetId(cursor.getString(cursor.getColumnIndex(ConversationBgColume._TARGETID)));
                conversationBg.setBgPath(cursor.getString(cursor.getColumnIndex(ConversationBgColume._BGPATH)));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return conversationBg;
    }

    @Override
    public int deleteById(String id) {
        int count;
        if (getById(id) != null) {
            count = getInstance().sqliteDB().delete(BsDbHelper.TABLE_CONVERSATION_BG,
                    ConversationBgColume._TARGETID + " = ? ", new String[] {
                        id
                    });

            return count;
        }
        return -1;

    }

    @Override
    public int insert(ConversationBg t) {
        if (getById(t.getTargetId()) != null) {
            return update(t);
        }
        ContentValues values = null;
        int result = 0;
        try {
            values = new ContentValues();
            values.put(ConversationBgColume._IMUSERID, t.getAccount());
            values.put(ConversationBgColume._TARGETID, t.getTargetId());
            values.put(ConversationBgColume._BGPATH, t.getBgPath());
            result = (int) getInstance().sqliteDB().insert(BsDbHelper.TABLE_CONVERSATION_BG, null, values);
            Log.i(TAG, "insert successfully result = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(ConversationBg t) {
        ContentValues values = null;
        int result = 0;
        try {
            values = new ContentValues();
            values.put(ConversationBgColume._BGPATH, t.getBgPath());
            result = (int) getInstance().sqliteDB().update(BsDbHelper.TABLE_CONVERSATION_BG, values, "_imuserid = ? and _targetId = ?", new String[] {
                    t.getAccount(), t.getTargetId()
            });
            Log.i(TAG, "update successfully result = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
