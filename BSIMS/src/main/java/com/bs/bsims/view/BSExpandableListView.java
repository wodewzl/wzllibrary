
package com.bs.bsims.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class BSExpandableListView extends ExpandableListView {

    public BSExpandableListView(Context context) {
        super(context);
    }

    public BSExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BSExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // TODO Auto-generated method stub

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
