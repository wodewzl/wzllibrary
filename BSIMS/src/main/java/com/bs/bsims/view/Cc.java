package com.bs.bsims.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.EmployeeVO;

import org.xutils.x;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public class Cc extends RelativeLayout {

	private int requestCode = 2011;
	private List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
	
	private Activity activity;
	
	@ViewInject(R.id.select_layout)
	private LinearLayout mParent;
	
	public Cc(Context context) {
		super(context);
	}

	public Cc(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Cc(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	
	
	private void init(Context context) {
		View root = View.inflate(context, R.layout.ui_cc, this);
		x.view().inject(this, root);
	}
	
	
	
	@Event(R.id.img_ui_addccname)
	public void addCcEmployeeVOs(View v) {
		//CustomToast.showShortToast(getContext(), "addCcEmployeeVOs()");
		
		Intent intent = new Intent();
//		intent.setClass(getContext(), PublicManyEmployeeActivity_new.class);
//		intent.putExtra("employ_name", LogReleaseActivity.class);
		intent.putExtra("checkboxlist", (Serializable) mDataList);
		intent.putExtra("requst_number", requestCode);

		activity.startActivityForResult(intent, requestCode);
		
	}
	
	
	public void setRightImage(boolean isAdd) {
		if(isAdd) {
			//默认是添加人员
		} else {
			ImageView rightImage = (ImageView) findViewById(R.id.img_ui_addccname);
			rightImage.setVisibility(View.GONE);
		}
	}
	
	
	
	public void setCallback(Activity activity, int requestCode) {
		this.activity = activity;
		this.requestCode = requestCode;
	}
	
	
	public void setDataList(List<EmployeeVO> mDataList) {
		this.mDataList = mDataList;
		mParent.removeAllViews();
		for (int i = 0; i < mDataList.size(); i++) {
			mParent.addView(getTextView(mDataList.get(i).getFullname()));
		}
	}
	
	
	public TextView getTextView(String name) {
		TextView txt = new TextView(getContext());
		android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 0, 0);
		txt.setLayoutParams(lp);
		txt.setTextColor(Color.parseColor("#7AC254"));
		txt.setText(name);
		txt.setTextSize(17f);
		return txt;
	}
	
	public interface CcCallback {
		void onClickAdd();
	}
	
	
}
