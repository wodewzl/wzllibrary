
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ContactDepTabResultVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CrmContactDetailActivity extends BaseActivity {

    private TextView lname, post, cname, address, birthday, hobby, addtime, fullname,
            staffPosition, okTv, head_momtv, jueCeRen, relationShip, gender, website;
    // private BSCircleImageView headpic;
    private BSCircleImageView headpic, lheadpic;
    private TextView mback;
    private ImageView sex, msgImage, telImage, callTel;
    private Drawable drawable;
    private LinearLayout callTelLayout, addMoreInfo, send_ly;

    private String lid = "";
    private ContactDepTabResultVO contactResultVo;
    private HeadAdapter Ccadapter;
    private GridView send_second_person_gv;
    private String[] array = {
            "编辑联系人", "共享联系人"
    };

    // flag1用于刷新联系人列表（由联系人也可进入详情编辑），flag1确定是否编辑了
    public Boolean flag1 = false;
    public static final String CONTACT_EDIT = "contact_edit";// 联系人编辑之后回来刷新

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crm_contact_detail_view, null);
        mContentLayout.addView(layout);
        lid = getIntent().getStringExtra("lid");

    }

    @Override
    public void initView() {
        // 页面头部自己编写的，没有应用,因为背景色不统一
        mHeadLayout.setVisibility(View.GONE);

        lname = (TextView) findViewById(R.id.lname);
        sex = (ImageView) findViewById(R.id.sex);
        post = (TextView) findViewById(R.id.post);
        cname = (TextView) findViewById(R.id.cname);
        address = (TextView) findViewById(R.id.address);
        birthday = (TextView) findViewById(R.id.birthday);
        hobby = (TextView) findViewById(R.id.hobby);
        addtime = (TextView) findViewById(R.id.addtime);
        fullname = (TextView) findViewById(R.id.fullname);
        staffPosition = (TextView) findViewById(R.id.staff_position);
        headpic = (BSCircleImageView) findViewById(R.id.headpic);
        lheadpic = (BSCircleImageView) findViewById(R.id.lheadpic);
        addMoreInfo = (LinearLayout) findViewById(R.id.addmoreinfo);
        okTv = (TextView) findViewById(R.id.head_right);
        head_momtv = (TextView) findViewById(R.id.head_momtv);
        send_ly = (LinearLayout) findViewById(R.id.send_ly);
        send_second_person_gv = (GridView) findViewById(R.id.send_second_person_gv);
        jueCeRen = (TextView) findViewById(R.id.jueceren);
        relationShip = (TextView) findViewById(R.id.intimacy);
        gender = (TextView) findViewById(R.id.gender);
        mback = (TextView) findViewById(R.id.mback);
        website = (TextView) findViewById(R.id.website);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    private boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            // map.put("userid", "22");
            map.put("lid", lid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            Gson gson1 = new Gson();
            String jsonUrlStr1 = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CONTACTS_DETAIL, map);
            contactResultVo = gson1.fromJson(jsonUrlStr1, ContactDepTabResultVO.class);

            if (Constant.RESULT_CODE.equals(contactResultVo.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateUi() {
        // 避免再次进入该界面时，以前添加的view还存在，造成数据重复
        addMoreInfo.removeAllViews();

        // 可编辑就显示“编辑”，否则隐藏
        contactResultVo = contactResultVo.getArray();
        if (contactResultVo.getCrmEdit().equals("1")) {
            okTv.setVisibility(View.VISIBLE);
        } else {
            okTv.setVisibility(View.GONE);
        }
        lname.setText(contactResultVo.getLname());

        if (CommonUtils.isNormalString(contactResultVo.getDepartment()) && CommonUtils.isNormalString(contactResultVo.getPost())) {
            post.setVisibility(View.VISIBLE);
            post.setText(contactResultVo.getDepartment() + "/" + contactResultVo.getPost());
        } else if (CommonUtils.isNormalString(contactResultVo.getDepartment()) && !CommonUtils.isNormalString(contactResultVo.getPost())) {
            post.setVisibility(View.VISIBLE);
            post.setText(contactResultVo.getDepartment());
        } else if (!CommonUtils.isNormalString(contactResultVo.getDepartment()) && CommonUtils.isNormalString(contactResultVo.getPost())) {
            post.setVisibility(View.VISIBLE);
            post.setText(contactResultVo.getPost());
        } else if (!CommonUtils.isNormalString(contactResultVo.getDepartment()) && !CommonUtils.isNormalString(contactResultVo.getPost())) {
            post.setVisibility(View.GONE);
        }

        cname.setText(contactResultVo.getCname());

        updateUIData(contactResultVo.getPhone(), "手机", R.drawable.crm_client_tel);
        updateUIData(contactResultVo.getTel(), "电话", R.drawable.crm_client_phone_black);
        updateUIData(contactResultVo.getWechat(), "微信", R.drawable.crm_client_weixin);
        updateUIData(contactResultVo.getQq(), "QQ", R.drawable.crm_client_qq_black);
        updateUIData(contactResultVo.getEmail(), "邮箱", R.drawable.crm_client_email);
        updateUIData(contactResultVo.getWeibo(), "微博", R.drawable.crm_weibo);
        updateUIData(contactResultVo.getFax(), "传真", R.drawable.crm_client_fax);

        if (CommonUtils.isNormalString(contactResultVo.getWebsite())) {
            website.setText(contactResultVo.getWebsite());
        } else {
            website.setText(null);
        }
        if (CommonUtils.isNormalString(contactResultVo.getAddress())) {
            address.setText(contactResultVo.getAddress());
        } else {
            address.setText(null);
        }
        if (CommonUtils.isNormalString(contactResultVo.getBirthday())) {
            birthday.setText(contactResultVo.getBirthday());
        }
        if (CommonUtils.isNormalString(contactResultVo.getHobby())) {
            hobby.setText(contactResultVo.getHobby());
        } else {
            hobby.setText(null);
        }
        if (CommonUtils.isNormalString(contactResultVo.getRelationship())) {
            jueCeRen.setText(contactResultVo.getRelationship());
        }
        if (CommonUtils.isNormalString(contactResultVo.getIntimacy())) {
            relationShip.setText(contactResultVo.getIntimacy());
        }
        addtime.setText("创建日期：" + DateUtils.parseDateDay(contactResultVo.getAddtime()));
        fullname.setText(contactResultVo.getFullname());
        staffPosition.setText(contactResultVo.getDname() + "/" + contactResultVo.getPname());

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(contactResultVo.getHeadpic(), headpic, options);
        headpic.setUserId(contactResultVo.getUserid());//HL:获取客户管理-联系人详情界面负责人的用户ID，以便实现跳转
        headpic.setUrl(contactResultVo.getHeadpic());
        headpic.setUserName(contactResultVo.getFullname());
        imageLoader.displayImage(contactResultVo.getLheadpic(), lheadpic, options);
        getSex();
        // 显示相关人
        if (null != contactResultVo.getInsUser())
            getFromUserToKeyset();
        else {
            head_momtv.setVisibility(View.GONE);
            send_ly.setVisibility(View.GONE);
        }
    }

    // 封装相关人的方法
    public void getFromUserToKeyset() {
        send_ly.setVisibility(View.VISIBLE);
        head_momtv.setVisibility(View.VISIBLE);
        Ccadapter = new HeadAdapter(this, false, true);
        Ccadapter.updateData(contactResultVo.getInsUser());
        send_second_person_gv.setAdapter(Ccadapter);
        Ccadapter.notifyDataSetChanged();
    }

    public void updateUIData(String[] strvo, String str, int picture) {
        // 设置手机电话号码
        if (strvo != null) {
            for (int i = 0; i < strvo.length; i++) {
                if (CommonUtils.isNormalString(strvo[i])) {
                    View optionsItem = View.inflate(CrmContactDetailActivity.this, R.layout.crm_contact_more_item, null);
                    ImageView image = (ImageView) optionsItem.findViewById(R.id.image);
                    TextView textstr = (TextView) optionsItem.findViewById(R.id.textstr);
                    addMoreInfo.addView(optionsItem);
                    image.setImageResource(picture);
                    textstr.setText(strvo[i]);
                }
            }
        }

    }

    public void getSex() {
        if (contactResultVo.getSex().equals("男")) {
            sex.setVisibility(View.VISIBLE);
            sex.setImageResource(R.drawable.sex_man);
            gender.setText(contactResultVo.getSex());
        } else if (contactResultVo.getSex().equals("女")) {
            sex.setVisibility(View.VISIBLE);
            sex.setImageResource(R.drawable.sex_woman);
            gender.setText(contactResultVo.getSex());
        } else {
            sex.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindViewsListener() {
        okTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 确定可编辑之后才可以跳转
                if (contactResultVo.getCrmEdit().equals("1")) {
                    CommonUtils.initPopViewBg(CrmContactDetailActivity.this, array, okTv, mCallback, CommonUtils.getScreenWidth(CrmContactDetailActivity.this) / 3);
                }
            }
        });

        mback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (flag1) {
                    // setResult(520, new Intent());
                    Intent in = new Intent(CONTACT_EDIT);
                    in.putExtra("lid", lid);
                    in.putExtra("name", contactResultVo.getLname());
                    in.putExtra("post", contactResultVo.getPost());
                    in.putExtra("headpic", contactResultVo.getLheadpic());
                    in.putExtra("sex", contactResultVo.getSex());
                    in.putExtra("cname", contactResultVo.getCname());
                    CrmContactDetailActivity.this.sendBroadcast(in);// 刷新客户主页的联系人列表数据
                }
                CrmContactDetailActivity.this.finish();
            }
        });
    }

    // 添加回调函数
    ResultCallback mCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            if (position == 0) {
                flag1 = true;
                Intent intent = new Intent();
                intent.setClass(CrmContactDetailActivity.this, CrmClientAddContactsActivity.class);
                intent.putExtra("vo", (Serializable) contactResultVo);
                // type=1表示从”联系人详情“跳转到“编辑联系人”界面，type=2表示从CrmClientHomeActivity跳过去，联系人新增和编辑共用一个界面
                intent.putExtra("type", "1");
                startActivityForResult(intent, 520);
            } else {
                Intent intent = new Intent();
                intent.setClass(CrmContactDetailActivity.this, CrmClientDifferentHeadActivity.class);
                // CurrentType值用于识别是从哪个界面跳过去的
                intent.putExtra("lid", lid);
                intent.putExtra("CurrentType", "1");
                if (contactResultVo.getInsUser() == null) {
                    intent.putExtra("relation", new ArrayList<EmployeeVO>());
                } else {
                    intent.putExtra("relation", contactResultVo.getInsUser());
                }
                startActivityForResult(intent, 520);
            }

        }
    };

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (intent != null) {
            new ThreadUtil(this, this).start();
        }
    }

    // finish当前界面，只有当详情编辑之后，才刷新联系人列表数据
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag1) {
                // setResult(520, new Intent());
                Intent in = new Intent(CONTACT_EDIT);
                in.putExtra("lid", lid);
                in.putExtra("name", contactResultVo.getLname());
                in.putExtra("post", contactResultVo.getPost());
                in.putExtra("headpic", contactResultVo.getLheadpic());
                in.putExtra("sex", contactResultVo.getSex());
                in.putExtra("cname", contactResultVo.getCname());
                CrmContactDetailActivity.this.sendBroadcast(in);// 刷新客户主页的联系人列表数据
            }
            CrmContactDetailActivity.this.finish();
        }
        return false;
    }

}
