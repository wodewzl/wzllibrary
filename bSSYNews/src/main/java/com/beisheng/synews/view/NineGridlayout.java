
package com.beisheng.synews.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.base.activity.ImagePreviewActivity;
import com.beisheng.base.utils.BaseCommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pan_ on 2015/2/2.
 */
public class NineGridlayout extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap;
    private int columns;//
    private int rows;//
    // private List listData;
    private int totalWidth;
    private List<String> listData = new ArrayList<String>();
    private Context mContext;
    public static final int MAX_COUNT = 9;

    public NineGridlayout(Context context) {
        super(context);
        this.mContext = context;
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        totalWidth = BaseCommonUtils.getScreenWidth(context) - BaseCommonUtils.dip2px(context, 80);
        gap = BaseCommonUtils.dip2px(context, 4);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView() {
        int childrenCount = listData.size();
        int singleWidth;
        int singleHeight;
        // if (childrenCount == 1) {
        // singleWidth = ((totalWidth - gap * (3 - 1)) / 3) * 2;
        // singleHeight = singleWidth;
        // } else {
        // singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        // singleHeight = singleWidth;
        // }

        singleWidth = (totalWidth - gap * (3 - 1));
        singleHeight = ((totalWidth - gap * (3 - 1)) / 3) * 2;

        // 根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount; i++) {
            if (i >= MAX_COUNT)
                break;
            final int index = i;
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            childrenView.setImageUrl(listData.get(i));
            childrenView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    onClickImage(index, listData.get(index));
                }
            });
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);

            if (i == MAX_COUNT - 1) {// 添加超过最大显示数量的文本
                int overCount = childrenCount - MAX_COUNT;
                if (overCount > 0) {
                    float textSize = 30;
                    final TextView textView = new TextView(mContext);
                    textView.setText("+" + String.valueOf(overCount));
                    textView.setTextColor(Color.WHITE);
                    textView.setPadding(0, singleHeight / 2 - getFontHeight(textSize), 0, 0);
                    textView.setTextSize(textSize);
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackgroundColor(Color.BLACK);
                    textView.getBackground().setAlpha(120);

                    textView.layout(left, top, right, bottom);
                    addView(textView);
                }
            }
        }

    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;// 行
                    position[1] = j;// 列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setImagesData(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        // 初始化布局
        generateChildrenLayout(list.size());
        // 这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < list.size()) {
                CustomImageView iv = generateImageView();
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = list.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView();

                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        // listData = list;
        listData.clear();
        listData.addAll(list);
        layoutChildrenView();
    }

    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }

        columns = 1;
        rows = length;
    }

    private CustomImageView generateImageView() {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    protected void onClickImage(int i, String url) {
        Intent intent = new Intent();
        ArrayList<String> list = (ArrayList<String>) listData;
        intent.putStringArrayListExtra("piclist", list);
        intent.setClass(mContext, ImagePreviewActivity.class);
        intent.putExtra("imgIndex", i);
        mContext.startActivity(intent);
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

}
