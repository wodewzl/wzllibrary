package com.wuzhanglong.library.utils;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;

import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;

import cn.bingoogolapple.androidcommon.adapter.BGADivider;
import cn.bingoogolapple.androidcommon.adapter.BGAGridDivider;

import static android.R.attr.spacing;

/**
 * Created by ${Wuzhanglong} on 2017/4/25.
 */

public class DividerUtil {

    public static DividerDecoration dividerPadding(Context context, int hight,int padding, int color){
        final DividerDecoration divider = new DividerDecoration.Builder(context)
                .setHeight(hight)
                .setPadding(padding)
                .setColorResource(color)
                .build();
        return divider;
    }

    public static DividerDecoration linnerDivider(Context context, int hight, int color) {
        DividerDecoration divider = new DividerDecoration.Builder(context)
                .setHeight(hight)
                .setColorResource(color)
                .build();
        return divider;
    }

    public static SpacesItemDecoration gridDividerSpc(Context context, int v, int h, int count, int color) {
        return SpacesItemDecoration.newInstance(spacing, spacing, count, ContextCompat.getColor(context, color));
    }

    public static GridItemDecoration gridDivider(Context context, int v, int h, int count, int color) {
        GridItemDecoration divider = new GridItemDecoration.Builder(context)
                .setHorizontal(h)
                .setVertical(v)
                .setColorResource(color)
                .build();
        return divider;
    }

    public static BGADivider bagDivider(int margintLeft, int margintRight) {

        BGADivider divider = BGADivider.newShapeDivider() //设置分割线,用BGADivider 可以去掉分类顶部的分割线
                .setMarginLeftDp(margintLeft)
                .setMarginRightDp(margintRight);
//                .setDelegate(new BGADivider.SimpleDelegate() {
//                    @Override
//                    public boolean isNeedSkip(int position, int itemCount) {
//                        // 如果是分类的话就跳过，顶部不绘制分隔线
//                        if (position / 5 == 0) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                });
        return divider;
    }

    public static BGAGridDivider bgaGridDivider( @DimenRes int spc){
        return  BGAGridDivider.newInstanceWithSpaceRes(spc);
    }
}
