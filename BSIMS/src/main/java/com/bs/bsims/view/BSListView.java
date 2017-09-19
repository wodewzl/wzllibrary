
package com.bs.bsims.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bs.bsims.R;

public class BSListView extends ListView {
    private LinearLayout headView;
    private LayoutInflater inflater;
    private RelativeLayout rootLayout;

    public BSListView(Context context) {
        super(context);
    }

    public BSListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (headView == null && this.getHeaderViewsCount() == 0) {
            inflater = LayoutInflater.from(context);
            headView = (LinearLayout) inflater.inflate(R.layout.list_head_refresh, null);
            rootLayout = (RelativeLayout) headView.findViewById(R.id.head_contentLayout);
            this.addHeaderView(headView);
            rootLayout.setVisibility(View.GONE);
        }
    }

    public BSListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void showHead(Context context, boolean isShow) {
        if (isShow) {
            rootLayout.setVisibility(View.VISIBLE);
        } else {
            rootLayout.setVisibility(View.GONE);
        }

    }
	@Override
	protected void dispatchDraw(Canvas canvas) {
		try {
			super.dispatchDraw(canvas);
		} catch (IndexOutOfBoundsException e) {
			// samsung error
		}
	}
}
