/**
 * 
 */

package com.bs.bsims.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmVisitorFromGaodeMap;
import com.bs.bsims.fragment.BaseFragment;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.image.selector.MultiImageSelectorActivity;
import com.bs.bsims.utils.FileUtil;

import java.io.File;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-4-21
 * @version 2.0
 */
public class ChatMessageMoreFragment extends BaseFragment implements OnClickListener {

    private static final String TAG = "ChatMessageMoreInfo";
    private static ChatMessageMoreFragment fragment;
    private Activity activity;
    private TextView textView1, textView2, textView3, textView4, textView5;
    private String shot_path;

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    public static ChatMessageMoreFragment newInstance() {
        if (fragment == null)
            fragment = new ChatMessageMoreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.im_chatmessage_more, container,
                false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvents();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }

    public void getImgpreview(int key) {

        switch (key) {
            case 1:
                Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                activity.startActivityForResult(intent, ImageActivityUtils.REQUEST_IMAGE);
                break;
            case 2:
                shot_path = FileUtil.getSaveFilePath(activity)
                        + System.currentTimeMillis() + ".jpg";
                File file = new File(shot_path);
                Intent intent2 = new Intent();
                intent2.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                activity.startActivityForResult(intent2, 101);
                break;

            case 3:
                Intent intent3 = new Intent();
                intent3.putExtra("key", "1");
                intent3.putExtra("isScreenShot", true);// 是否需要地图截屏
                intent3.setClass(activity, CrmVisitorFromGaodeMap.class);
                activity.startActivityForResult(intent3, 102);
                break;

            default:
                break;
        }

    }

    public void initView(View view) {
        textView1 = (TextView) view.findViewById(R.id.send_img);
        textView2 = (TextView) view.findViewById(R.id.send_photo);
        textView3 = (TextView) view.findViewById(R.id.send_address);
        textView4 = (TextView) view.findViewById(R.id.send_video);
        textView5 = (TextView) view.findViewById(R.id.send_voice);
    }

    public void initEvents() {
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.send_img:
                getImgpreview(1);
                break;
            case R.id.send_photo:
                getImgpreview(2);
                break;
            case R.id.send_address:
                getImgpreview(3);
                break;

            default:
                break;
        }
    }

    public String getShot_path() {
        return shot_path;
    }

    public void setShot_path(String shot_path) {
        this.shot_path = shot_path;
    }

}
