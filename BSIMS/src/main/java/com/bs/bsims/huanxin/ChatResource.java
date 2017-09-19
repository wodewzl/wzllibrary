
package com.bs.bsims.huanxin;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bs.bsims.R;

import java.util.ArrayList;
import java.util.List;

public class ChatResource {

    private static ChatResource instance;

    private ChatResource() {
    }

    public static ChatResource getInstance() {
        if (null == instance)
            instance = new ChatResource();
        return instance;
    }

    /**
     * 获取录音时的动画数组
     * 
     * @return
     */
    public Drawable[] getMicImages(Context context) {
        Drawable[] micImages = new Drawable[] {
                context.getResources().getDrawable(R.drawable.record_animate_01),
                context.getResources().getDrawable(R.drawable.record_animate_02),
                context.getResources().getDrawable(R.drawable.record_animate_03),
                context.getResources().getDrawable(R.drawable.record_animate_04),
                context.getResources().getDrawable(R.drawable.record_animate_05),
                context.getResources().getDrawable(R.drawable.record_animate_06),
                context.getResources().getDrawable(R.drawable.record_animate_07),
                context.getResources().getDrawable(R.drawable.record_animate_08),
                context.getResources().getDrawable(R.drawable.record_animate_09),
                context.getResources().getDrawable(R.drawable.record_animate_10),
                context.getResources().getDrawable(R.drawable.record_animate_11),
                context.getResources().getDrawable(R.drawable.record_animate_12),
                context.getResources().getDrawable(R.drawable.record_animate_13),
                context.getResources().getDrawable(R.drawable.record_animate_14),
        };
        return micImages;
    }

    /**
     * 获取表情文件的List
     * 
     * @param getSum
     * @return
     */
    public List<String> getExpressionRes(int getSum) {
        getSum = 35; // 一共35个文件
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;
            reslist.add(filename);
        }
        return reslist;
    }


    /**
     * 获取表情文件的List
     * 
     * @param getSum
     * @return
     */
    public List<String> getExpressionResNo35(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;
            reslist.add(filename);
        }
        return reslist;
    }
}
