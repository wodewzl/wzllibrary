
package com.beisheng.synews.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ColumnHorizontalScrollView extends HorizontalScrollView {
    /** 浼犲叆鏁翠綋甯冨眬 */
    private View ll_content;
    /** 浼犲叆鏇村鏍忕洰閫夋嫨甯冨眬 */
    private View ll_more;
    /** 浼犲叆鎷栧姩鏍忓竷灞� */
    private View rl_column;
    /** 宸﹂槾褰卞浘鐗� */
    private ImageView leftImage;
    /** 鍙抽槾褰卞浘鐗� */
    private ImageView rightImage;
    /** 灞忓箷瀹藉害 */
    private int mScreenWitdh = 0;
    /** 鐖剁被鐨勬椿鍔╝ctivity */
    private Activity activity;

    public ColumnHorizontalScrollView(Context context) {
        super(context);
    }

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 鍦ㄦ嫋鍔ㄧ殑鏃跺�欐墽琛�
     */
    @Override
    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        shade_ShowOrHide();
        if (!activity.isFinishing() && ll_content != null && leftImage != null
                && rightImage != null && ll_more != null && rl_column != null) {
            if (ll_content.getWidth() <= mScreenWitdh) {
                leftImage.setVisibility(View.GONE);
                rightImage.setVisibility(View.GONE);
            }
        } else {
            return;
        }
        if (paramInt1 == 0) {
            leftImage.setVisibility(View.GONE);
            rightImage.setVisibility(View.VISIBLE);
            return;
        }
        if (ll_content.getWidth() - paramInt1 + ll_more.getWidth() + rl_column.getLeft() == mScreenWitdh) {
            leftImage.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.GONE);
            return;
        }
        leftImage.setVisibility(View.VISIBLE);
        rightImage.setVisibility(View.VISIBLE);
    }

    /**
     * 浼犲叆鐖剁被甯冨眬涓殑璧勬簮鏂囦欢
     */
    public void setParam(Activity activity, int mScreenWitdh, View paramView1,
            ImageView paramView2, ImageView paramView3, View paramView4, View paramView5) {
        this.activity = activity;
        this.mScreenWitdh = mScreenWitdh;
        ll_content = paramView1;
        leftImage = paramView2;
        rightImage = paramView3;
        ll_more = paramView4;
        rl_column = paramView5;
    }

    /**
     * 鍒ゆ柇宸﹀彸闃村奖鐨勬樉绀洪殣钘忔晥鏋�
     */
    public void shade_ShowOrHide() {
        if (!activity.isFinishing() && ll_content != null) {
            measure(0, 0);
            // 濡傛灉鏁翠綋瀹藉害灏忎簬灞忓箷瀹藉害鐨勮瘽锛岄偅宸﹀彸闃村奖閮介殣钘�
            if (mScreenWitdh >= getMeasuredWidth()) {
                leftImage.setVisibility(View.GONE);
                rightImage.setVisibility(View.GONE);
            }
        } else {
            return;
        }
        // 濡傛灉婊戝姩鍦ㄦ渶宸﹁竟鏃跺�欙紝宸﹁竟闃村奖闅愯棌锛屽彸杈规樉绀�
        if (getLeft() == 0) {
            leftImage.setVisibility(View.GONE);
            rightImage.setVisibility(View.VISIBLE);
            return;
        }
        // 濡傛灉婊戝姩鍦ㄦ渶鍙宠竟鏃跺�欙紝宸﹁竟闃村奖鏄剧ず锛屽彸杈归殣钘�
        if (getRight() == getMeasuredWidth() - mScreenWitdh) {
            leftImage.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.GONE);
            return;
        }
        // 鍚﹀垯锛岃鏄庡湪涓棿浣嶇疆锛屽乏銆佸彸闃村奖閮芥樉绀�
        leftImage.setVisibility(View.VISIBLE);
        rightImage.setVisibility(View.VISIBLE);
    }
}
