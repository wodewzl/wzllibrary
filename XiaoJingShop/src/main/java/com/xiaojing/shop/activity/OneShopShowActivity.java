package com.xiaojing.shop.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.FileUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.xiaojing.shop.R.id.img;

public class OneShopShowActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks,
        View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private List<File> mFileList = new ArrayList<>();
    private ImageView mImg;
    private TextView mNameTv, mNumberTv;
    private EditText mContentEt;
    private Button mCommtBt;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.show_shop_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("晒单分享");
        mPhotosSnpl = getViewById(R.id.snpl_moment_add_photos);
        if (false) {
            //单选
            mPhotosSnpl.setData(null);
            mPhotosSnpl.setMaxItemCount(1);
        } else {
            //多选
            mPhotosSnpl.setMaxItemCount(3);
        }
        mPhotosSnpl.setEditable(true);//有加号，有删除，可以点加号选择，false没有加号，点其他按钮选择，也没有删除
        mPhotosSnpl.setPlusEnable(true);//有加号，可以点加号选择，false没有加号，点其他按钮选择
        mPhotosSnpl.setSortable(true);//排序

        mImg = getViewById(img);
        Picasso.with(this).load(this.getIntent().getStringExtra("img")).into(mImg);
        mNameTv = getViewById(R.id.name_tv);
        mNameTv.setText(this.getIntent().getStringExtra("name"));
        mNumberTv = getViewById(R.id.number_tv);
        mNumberTv.setText("本期期号：" + this.getIntent().getStringExtra("number"));
        mContentEt = getViewById(R.id.content_et);
        mCommtBt = getViewById(R.id.commit_bt);
        mCommtBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor4, R.color.XJColor4));
    }

    @Override
    public void bindViewsListener() {
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        mCommtBt.setOnClickListener(this);
    }

    public static void startAction(Context mContext, View view, String id, String img, String name, String number) {
        Intent intent = new Intent(mContext, OneShopShowActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("img", img);
        intent.putExtra("number", number);
        intent.putExtra("name", name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) mContext, view, BaseConstant.TRANSITION_ANIMATION_NEWS_PHOTOS);
            mContext.startActivity(intent, options.toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
        }

    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_bt:
                if (mContentEt.getText().length() == 0) {
                    showCustomToast("请填写获奖感言");
                    return;
                }
                commitUserInfo();
                break;
            default:
                break;
        }
    }


    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View
            view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);

    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
//            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
            File takePhotoDir = new File(FileUtil.getSaveFilePath(this, Constant.SDCARD_CACHE));

            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }


    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            //是否单选，单选走true 语句，多选走false语句，这么默认false
            if (false) {
                mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedImages(data));
            } else {
                mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }

        mFileList.clear();
        for (int i = 0; i < mPhotosSnpl.getData().size(); i++) {
            File file = new File(mPhotosSnpl.getData().get(i));
            File newFile = CompressHelper.getDefault(this).compressToFile(file);
            mFileList.add(newFile);
        }
    }

    public void commitUserInfo() {
        try {
            showProgressDialog();
            final RequestParams paramsMap = new RequestParams();
            paramsMap.put("od_id", this.getIntent().getStringExtra("id"));
            paramsMap.put("od_no", this.getIntent().getStringExtra("number"));
            paramsMap.put("goods_name", this.getIntent().getStringExtra("name"));
            paramsMap.put("share_content", mContentEt.getText().toString());
            if (AppApplication.getInstance().getUserInfoVO() != null)
                paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            for (int i = 0; i < mFileList.size(); i++) {
                paramsMap.put("file" + i, mFileList.get(i));
            }
            String mUrl = Constant.ONE_SHOP_SHOW_URL;
            HttpClientUtil.post(mActivity, this, mUrl, paramsMap, null, "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
