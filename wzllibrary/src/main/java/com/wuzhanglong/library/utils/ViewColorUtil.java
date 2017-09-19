package com.wuzhanglong.library.utils;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.vondear.rxtools.RxAnimationUtils;
import com.vondear.rxtools.interfaces.onUpdateListener;

/**
 * Created by ${Wuzhanglong} on 2017/4/24.
 */

public class ViewColorUtil {
    //颜色从开始，中间，结束三个颜色值渐变 type 为渐变方向 1 TOP_BOTTOM 2 BOTTOM_TOP
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void viewGradient(int[] colors, View view, int type) {
//        int colors[] = { 0xff2D0081 , 0xff8B3097, 0xffD14E7A };
        GradientDrawable.Orientation orientation;
        if (type == 1) {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        } else {
            orientation = GradientDrawable.Orientation.BOTTOM_TOP;
        }
        GradientDrawable bg = new GradientDrawable(orientation,colors);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(bg);
        } else {
            view.setBackground(bg);
        }
    }

    //颜色从开始渐变到结束 短暂的动画过程
    public static void viewGradientAnimation(int startColor, int endColor, final View view) {
        RxAnimationUtils.animationColorGradient(startColor, endColor, new onUpdateListener() {
            @Override
            public void onUpdate(int i) {
                view.setBackgroundColor(i);
            }
        });
    }
}
