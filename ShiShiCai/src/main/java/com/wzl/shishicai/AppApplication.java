
package com.wzl.shishicai;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.wuzhanglong.library.application.BaseAppApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class AppApplication extends BaseAppApplication {
    private static AppApplication mAppApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
    }

    /**
     * 获取Application
     */
    public static AppApplication getInstance() {
        return mAppApplication;
    }

    public List<DataVO> getDataVO() {
        try {
            FileInputStream stream = this.openFileInput("data.DataVO");
            ObjectInputStream ois = new ObjectInputStream(stream);
            List<DataVO> list = (List<DataVO>) ois.readObject();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDataVO(List<DataVO> list) {
        try {
            FileOutputStream stream = this.openFileOutput("data.DataVO", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(list);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<DataVO> getDataVO3() {
        try {
            FileInputStream stream = this.openFileInput("data.DataVO3");
            ObjectInputStream ois = new ObjectInputStream(stream);
            List<DataVO> list = (List<DataVO>) ois.readObject();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDataVO3(List<DataVO> list) {
        try {
            FileOutputStream stream = this.openFileOutput("data.DataVO3", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(list);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
