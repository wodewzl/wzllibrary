package com.xiaojing.shop.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.FileUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.CityVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.xiaojing.shop.constant.Constant.SHOP_JOIN_URL;

public class RegistActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener, PostCallback {
    private BGASortableNinePhotoLayout mPhotoLayout01, mPhotoLayout02, mPhotoLayout03;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private CheckBox mShopUser, mUser,mAgreement;
    private Button mCodeBt;
    private boolean mCodeStae = true;
    private EditText mEditText01, mEditText02, mEditText03, mEditText04, mEditText05, mEditText06, mEditText07, mEditText09, mEditText12;
    private TextView mTextView08, mTextView10, mTextView11;
    private String mUserType = "0";
    private Button mCommitBt;
    private List<File> mFileList01 = new ArrayList<>();
    private List<File> mFileList02 = new ArrayList<>();
    private List<File> mFileList03 = new ArrayList<>();
    private LinearLayout mShopLayout, mUserLayout,mAggrementLayout;
    private boolean mFlag = false;
    private int mCurrentClickPhoto = 0;//1、2、3一次为 mPhotoLayout01,mPhotoLayout02,mPhotoLayout03
    private ArrayList<CityVO> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<CityVO>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<CityVO>>> options3Items = new ArrayList<>();
    private String mProvinceId, mCityId, mAreaId;
    private String mLat, mLon;
    private String mShopType = "";

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.regist_activity);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mShopUser = getViewById(R.id.shop_user);
        mUser = getViewById(R.id.user);
        mCodeBt = getViewById(R.id.code_bt);
        mAgreement=getViewById(R.id.agree_cb);
        mEditText01 = getViewById(R.id.et_01);
        mEditText02 = getViewById(R.id.et_02);
        mEditText03 = getViewById(R.id.et_03);
        mEditText04 = getViewById(R.id.et_04);
        mEditText05 = getViewById(R.id.et_05);
        mEditText06 = getViewById(R.id.et_06);
        mEditText07 = getViewById(R.id.et_07);
        mTextView08 = getViewById(R.id.tv_08);
        mTextView08.setText(SharePreferenceUtil.getSharedpreferenceValue(this, "address", "detail_address"));
        mLat = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lat");
        mLon = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lo");

        mEditText09 = getViewById(R.id.et_09);
        mTextView10 = getViewById(R.id.tv_10);
        mTextView11 = getViewById(R.id.tv_11);
        mEditText12 = getViewById(R.id.et_12);
        mCommitBt = getViewById(R.id.commit_bt);
        mCommitBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, BaseCommonUtils.dip2px(this, 2), R.color.C7, R.color.C7));
        mShopLayout = getViewById(R.id.shop_layout);
        mUserLayout = getViewById(R.id.user_layout);
        mUser.setChecked(true);
        mCodeBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C10, R.color.C10));

        mPhotoLayout01 = getViewById(R.id.phone_layout01);
        mPhotoLayout02 = getViewById(R.id.phone_layout02);
        mPhotoLayout03 = getViewById(R.id.phone_layout03);

        mPhotoLayout01.setMaxItemCount(1);
        mPhotoLayout01.setEditable(true);//有加号，有删除，可以点加号选择，false没有加号，点其他按钮选择，也没有删除
        mPhotoLayout01.setPlusEnable(true);//有加号，可以点加号选择，false没有加号，点其他按钮选择
        mPhotoLayout01.setSortable(true);//排序

        mPhotoLayout03.setMaxItemCount(3);
        mPhotoLayout03.setEditable(true);//有加号，有删除，可以点加号选择，false没有加号，点其他按钮选择，也没有删除
        mPhotoLayout03.setPlusEnable(true);//有加号，可以点加号选择，false没有加号，点其他按钮选择
        mPhotoLayout03.setSortable(true);//排序
        if ("2".equals(this.getIntent().getStringExtra("type"))) {
            mBaseTitleTv.setText("申请加盟");
            mCommitBt.setText("加盟");
            mUserLayout.setVisibility(View.GONE);
            mShopLayout.setVisibility(View.VISIBLE);
        } else {
            mBaseTitleTv.setText("注册");
            mCommitBt.setText("注册");

        }

        mAggrementLayout=getViewById(R.id.aggrement_layout);
    }

    @Override
    public void bindViewsListener() {
        mTextView08.setOnClickListener(this);
        mTextView10.setOnClickListener(this);
        mTextView11.setOnClickListener(this);
        // 设置拖拽排序控件的代理
        mPhotoLayout01.setDelegate(this);
        mPhotoLayout02.setDelegate(this);
        mPhotoLayout03.setDelegate(this);
        mShopUser.setOnCheckedChangeListener(this);
        mUser.setOnCheckedChangeListener(this);
        mAgreement.setOnCheckedChangeListener(this);
        mCodeBt.setOnClickListener(this);
        mCommitBt.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mAggrementLayout.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.GET_CITY_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, CityVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        CityVO cityVO = (CityVO) vo;
        CityVO mCityVO = ((CityVO) vo).getDatas();
        options1Items = mCityVO.getArea_list();

        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
            ArrayList<CityVO> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<CityVO>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < options1Items.get(i).getCitys().size(); c++) {//遍历该省份的所有城市
                CityVO city = options1Items.get(i).getCitys().get(c);
                CityList.add(city);//添加城市
                ArrayList<CityVO> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (options1Items.get(i).getCitys().get(c).getDistricts() == null
                        || options1Items.get(i).getCitys().get(c).getDistricts().size() == 0) {
                    City_AreaList.add(new CityVO());
                } else {
                    for (int d = 0; d < options1Items.get(i).getCitys().get(c).getDistricts().size(); d++) {//该城市对应地区所有数据
                        CityVO districts = options1Items.get(i).getCitys().get(c).getDistricts().get(d);
                        City_AreaList.add(districts);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            options2Items.add(CityList);
            options3Items.add(Province_AreaList);
        }
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View
            view, int position, ArrayList<String> models) {

        if (mPhotoLayout01 == sortableNinePhotoLayout) {
            mCurrentClickPhoto = 1;
        } else if (mPhotoLayout02 == sortableNinePhotoLayout) {
            mCurrentClickPhoto = 2;
        } else {
            mCurrentClickPhoto = 3;
        }
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

        if (mPhotoLayout01 == sortableNinePhotoLayout) {
            mPhotoLayout01.removeItem(position);
        } else if (mPhotoLayout02 == sortableNinePhotoLayout) {
            mPhotoLayout02.removeItem(position);
        } else {
            mPhotoLayout03.removeItem(position);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (mPhotoLayout01 == sortableNinePhotoLayout) {
            startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotoLayout01.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
        } else if (mPhotoLayout02 == sortableNinePhotoLayout) {
            startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotoLayout02.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
        } else {
            startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotoLayout03.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
//            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
            File takePhotoDir = new File(FileUtil.getSaveFilePath(this, Constant.SDCARD_CACHE));

            if (mCurrentClickPhoto == 1) {
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotoLayout01.getMaxItemCount() - mPhotoLayout01.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
            } else if (mCurrentClickPhoto == 2) {
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotoLayout02.getMaxItemCount() - mPhotoLayout02.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
            } else {
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotoLayout03.getMaxItemCount() - mPhotoLayout03.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
            }
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
            if (mCurrentClickPhoto == 1) {
                mPhotoLayout01.setData(BGAPhotoPickerActivity.getSelectedImages(data));
            } else if (mCurrentClickPhoto == 2) {
                mPhotoLayout02.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
            } else {
                mPhotoLayout03.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));

            }
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            if (mCurrentClickPhoto == 1) {
                mPhotoLayout01.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
            } else if (mCurrentClickPhoto == 2) {
                mPhotoLayout02.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
            } else {
                mPhotoLayout03.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
            }
        }

        if (mCurrentClickPhoto == 1) {
            mFileList01.clear();
            for (int i = 0; i < mPhotoLayout01.getData().size(); i++) {
                File file = new File(mPhotoLayout01.getData().get(i));
                File newFile = CompressHelper.getDefault(RegistActivity.this).compressToFile(file);
                mFileList01.add(newFile);
            }
        } else if (mCurrentClickPhoto == 2) {
            mFileList02.clear();
            for (int i = 0; i < mPhotoLayout02.getData().size(); i++) {
                File file = new File(mPhotoLayout02.getData().get(i));
                File newFile = CompressHelper.getDefault(RegistActivity.this).compressToFile(file);
                mFileList02.add(newFile);
            }
        } else {
            mFileList03.clear();
            for (int i = 0; i < mPhotoLayout03.getData().size(); i++) {
                File file = new File(mPhotoLayout03.getData().get(i));
                File newFile = CompressHelper.getDefault(RegistActivity.this).compressToFile(file);
                mFileList03.add(newFile);
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.shop_user:
                if (b) {
                    mUserType = "1";
                    mUser.setChecked(false);
                    mShopLayout.setVisibility(View.VISIBLE);
                } else {
                    mUser.setChecked(true);
                    mShopLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.user:
                if (b) {
                    mUserType = "0";
                    mShopUser.setChecked(false);
                } else {
                    mShopUser.setChecked(true);
                }
                break;


            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_bt:

                if (mCodeStae) {
                    mCodeStae = false;
                    getCode();
                    DaoJishi();
                }
                break;
            case R.id.commit_bt:
//                showProgressDialog();
                if (!"2".equals(this.getIntent().getStringExtra("type"))) {
                    if (mEditText01.getText().toString() == null) {
                        showCustomToast("手机号不能为空");
                        return;
                    }
                    if (mEditText02.getText().toString() == null) {
                        showCustomToast("短信验证码不能为空");
                        return;
                    }
                    if (mEditText03.getText().toString() == null || mEditText04.getText().toString() == null) {
                        showCustomToast("密码不能为空");
                        return;
                    }
                    if (mEditText03.getText().length() < 6 || mEditText04.getText().length() < 6) {
                        showCustomToast("密码长度必须6位以上");
                        return;
                    }
                }
                if ("1".equals(mUserType) || "2".equals(this.getIntent().getStringExtra("type"))) {
                    if (mEditText06.getText().toString() == null) {
                        showCustomToast("商铺名称不能为空");
                        return;
                    }
                    if (mEditText07.getText().toString() == null) {
                        showCustomToast("商铺联系人不能为空");
                        return;
                    }

                    if (mTextView08.getText().toString() == null) {
                        showCustomToast("请填写商铺地址");
                        return;
                    }

                    if (mEditText09.getText().toString() == null) {
                        showCustomToast("请填写营业时间");
                        return;
                    }

                    if (mTextView10.getText().toString() == null) {
                        showCustomToast("请选择商铺区域");
                        return;
                    }

                    if (mTextView11.getText().toString() == null) {
                        showCustomToast("请选择营业类型");
                        return;
                    }


                    if (mFileList01.size() == 0) {
                        showCustomToast("请上传门面照片");
                        return;
                    }
                    if (mFileList02.size() == 0) {
                        showCustomToast("请上传实体照片");
                        return;
                    }
                    if (mFileList01.size() == 0) {
                        showCustomToast("请上传营业执照照片");
                        return;
                    }
                }

                if(!mAgreement.isChecked()){
                    showCustomToast("请仔细阅读用户协议并同意");
                    return;
                }

                try {
                    commitUserInfo();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_10:
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = options1Items.get(options1).getProvince_name()
                                + options2Items.get(options1).get(option2).getCity_name()
                                + options3Items.get(options1).get(option2).get(options3).getDistrict_name();
                        mTextView10.setText(tx);

                        mProvinceId = options1Items.get(options1).getProvince_id();
                        mCityId = options2Items.get(options1).get(option2).getCity_id();
                        mAreaId = options3Items.get(options1).get(option2).get(options3).getDistrict_id();
                    }
                }).build();
                pvOptions.setPicker(options1Items, options2Items, options3Items);
                pvOptions.show();
                break;
            case R.id.tv_08:
                openActivity(AddressMapActivity.class);
                break;
            case R.id.tv_11:
                openActivity(ShopTypeActivity.class);
                break;
            case R.id.aggrement_layout:
                Bundle bundle=new Bundle();
                bundle.putString("url",SharePreferenceUtil.getSharedpreferenceValue(this,"agreement","agreement_url"));
                open(WebViewActivity.class,bundle,0);
                break;
            default:
                break;
        }
    }

    public void DaoJishi() {
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long arg0) {
                mCodeBt.setText("发送验证码(" + arg0 / 1000 + ")");
            }

            @Override
            public void onFinish() {
                mCodeBt.setText("获取验证码");
                mCodeStae = true;
            }
        };
        timer.start();
    }

    public void getCode() {
        RequestParams paramsMap = new RequestParams();
        paramsMap.put("phone", mEditText01.getText().toString());
        paramsMap.put("type", "1");// 1-手机号注册 2-手机动态码登录 3-找回密码
        String mUrl = Constant.GET_CODE;
        HttpClientUtil.post(mActivity, this, mUrl, paramsMap, null,"1");
    }

    public void commitUserInfo() throws FileNotFoundException {
        showProgressDialog();
        final RequestParams paramsMap = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("phone", mEditText01.getText().toString());
        paramsMap.put("captcha", mEditText02.getText().toString());
        paramsMap.put("password", mEditText03.getText().toString());
        paramsMap.put("re_password", mEditText04.getText().toString());
        paramsMap.put("invite_code", mEditText05.getText().toString());
        paramsMap.put("is_merchant", mUserType); //0-普通会员 1-商户
        paramsMap.put("merchant_name", mEditText06.getText().toString());
        paramsMap.put("contact_name", mEditText07.getText().toString());
        paramsMap.put("merchant_address", mTextView08.getText().toString());
        paramsMap.put("merchant_lng", mLon);
        paramsMap.put("merchant_lat", mLat);
        paramsMap.put("merchant_class", mShopType);
        paramsMap.put("merchant_worktime", mEditText09.getText().toString());
        paramsMap.put("merchant_introduce", mEditText12.getText().toString());
        paramsMap.put("area_id", mAreaId);
        paramsMap.put("area_info", mTextView10.getText().toString());

        for (int i = 0; i < mFileList01.size(); i++) {
            paramsMap.put("logo", mFileList01.get(i));
        }
        for (int i = 0; i < mFileList02.size(); i++) {
            paramsMap.put("image" + i, mFileList02.get(i));
        }
        for (int i = 0; i < mFileList03.size(); i++) {
            paramsMap.put("file" + i, mFileList03.get(i));
        }
        String url = "";
        if ("2".equals(this.getIntent().getStringExtra("type"))) {
            url = SHOP_JOIN_URL;
        } else {
            url = Constant.REGIST_URL;
        }
        HttpClientUtil.post(mActivity, this, url, paramsMap, null, this);
    }

    public void getCityData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.GET_CITY_URL;
        HttpClientUtil.post(mActivity, this, mUrl, paramsMap, CityVO.class, this);
    }

    @Override
    public void success(BaseVO vo) {

        if (BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
            if ("2".equals(this.getIntent().getStringExtra("type"))) {
                new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("友情提示")
                        .setContentText("您的资料已提交，平台将在1-5个工作日审核您的资料")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                RegistActivity.this.finish();
                                sDialog.dismissWithAnimation();//直接消失
                            }
                        })
                        .show();
            } else if (mUserType == "1") {
                new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("友情提示")
                        .setContentText("您的资料已提交，平台将在1-5个工作日审核您的资料")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                RegistActivity.this.finish();
                                sDialog.dismissWithAnimation();//直接消失
                            }
                        })
                        .show();
            } else {
                showCustomToast("注册成功");
                this.finish();
            }
        } else {
            showCustomToast(vo.getError());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("address_map".equals(event.getMessage())) {
            String[] params = event.getParams();
            mLat = params[2];
            mLon = params[3];
            mTextView08.setText(params[1] + params[0]);
        }
        if ("shop_type".equals(event.getMessage())) {
            String[] params = event.getParams();
            mTextView11.setText(params[0]);
            mShopType = params[1];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}