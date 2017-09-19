
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.DanganBasicinfolearnexpapdater;
import com.bs.bsims.adapter.DanganBasicinfoworkexpapdater;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.DanganBasicinfo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class DanganJiBenInfoFragment extends BaseFragment implements
        UpdateCallback {

    private String uid;
    private Activity mActivity;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private TextView basicinfofullname, basicinfodpname, basicinfobirthday,
            basicinfosex, basicinfohometown, basicinfoaccounts,
            basicinfonation, basicinfoheight, basicinfomarriage,
            basicinfobirth, basicinfoaddress, basicinfoentrytime,
            basicinfopositivetime, loading;

    private LinearLayout baiscinfoworkly, baiscinfolearnly, baicinfomain,
            loading_layout;
    private FrameLayout baiscinfolearnfy,basicinfoworkexpfy;
    
    private ImageView basicinfoheadpic, basicinfoseximg;

    private DanganBasicinfo basicinfo;

    private List<DanganBasicinfo> array;

    private BSListView basicinfoeduexp;

    private BSListView basicinfoworkexp;

    private DanganBasicinfoworkexpapdater basicinfoworkexpadapter;

    private DanganBasicinfolearnexpapdater basicinfolearnexpadapter;

    public DanganJiBenInfoFragment(String uid) {
        this.uid = uid;
    }

    public DanganJiBenInfoFragment() {

    }

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
        options = CommonUtils.initImageLoaderOptions();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.da_jibeninfo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        basicinfoworkexpadapter = new DanganBasicinfoworkexpapdater(mActivity);
        basicinfolearnexpadapter = new DanganBasicinfolearnexpapdater(mActivity);
        // bindListers();
    }

    private void initViews(View view) {
        loading = (TextView) view.findViewById(R.id.loading);
        loading_layout = (LinearLayout) view.findViewById(R.id.loading_layout);
        baicinfomain = (LinearLayout) view.findViewById(R.id.baicinfomain);
        baiscinfoworkly = (LinearLayout) view
                .findViewById(R.id.baiscinfoworkly);
        baiscinfolearnly = (LinearLayout) view
                .findViewById(R.id.baiscinfolearnly);
        // TODO Auto-generated method stub
        basicinfofullname = (TextView) view
                .findViewById(R.id.basicinfofullname);
        basicinfodpname = (TextView) view.findViewById(R.id.basicinfodpname);
        basicinfobirthday = (TextView) view
                .findViewById(R.id.basicinfobirthday);
        basicinfosex = (TextView) view.findViewById(R.id.basicinfosex);
        basicinfoaccounts = (TextView) view
                .findViewById(R.id.basicinfoaccounts);
        basicinfohometown = (TextView) view
                .findViewById(R.id.basicinfohometown);
        basicinfonation = (TextView) view.findViewById(R.id.basicinfonation);
        basicinfoheight = (TextView) view.findViewById(R.id.basicinfoheight);
        basicinfobirth = (TextView) view.findViewById(R.id.basicinfobirth);
        basicinfoaddress = (TextView) view.findViewById(R.id.basicinfoaddress);
        basicinfoentrytime = (TextView) view
                .findViewById(R.id.basicinfoentrytime);
        basicinfopositivetime = (TextView) view
                .findViewById(R.id.basicinfopositivetime);
        basicinfomarriage = (TextView) view
                .findViewById(R.id.basicinfomarriage);

        basicinfoheadpic = (ImageView) view.findViewById(R.id.basicinfoheadpic);
        basicinfoseximg = (ImageView) view.findViewById(R.id.basicinfoseximg);

        // 教育经历
        basicinfoworkexp = (BSListView) view
                .findViewById(R.id.basicinfoworkexp);
        basicinfoeduexp = (BSListView) view.findViewById(R.id.basicinfoeduexp);
        baiscinfolearnfy =(FrameLayout) view.findViewById(R.id.basicinfoeduexp_fl);
        basicinfoworkexpfy =(FrameLayout) view.findViewById(R.id.basicinfoworkexp_fl);
        

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        if (null == basicinfo) {
            new ThreadUtil(mActivity, this).start();
        }
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getdata();
    }

    @Override
    public void executeSuccess() {

        loading_layout.setVisibility(View.GONE);
        baicinfomain.setVisibility(View.VISIBLE);
        if (null == array && !checkViarbleisNull()) {
            return;
        }
        // TODO Auto-generated method stub
        basicinfofullname.setText(array.get(0).getFullname());
        basicinfodpname.setText(array.get(0).getDname() + "/"
                + array.get(0).getPname());

        if (!array.get(0).getSex().equals("")
                && array.get(0).getSex().equals("男")) {
            basicinfoseximg.setBackgroundResource(R.drawable.sex_man);
        } else {
            basicinfoseximg.setBackgroundResource(R.drawable.sex_woman);
        }
        basicinfobirthday.setText(DateUtils.parseDateDay(array.get(0)
                .getBirthday()));
        basicinfosex.setText(array.get(0).getSex());

        if (array.get(0).getAccounts().length() > 5) {
            basicinfoaccounts.setText(array.get(0).getAccounts()
                    .substring(0, 2)
                    + array.get(0).getAccounts().substring(3, 5));
        } else {
            basicinfoaccounts.setText(array.get(0).getAccounts());
        }
        if (array.get(0).getHometown().length() > 6) {
            basicinfohometown.setText(array.get(0).getHometown().substring(0, 6));
        }
        else {
            basicinfohometown.setText(array.get(0).getHometown());
        }
        if (array.get(0).getNation().length() > 6) {
            basicinfonation.setText(array.get(0).getNation().substring(0, 6));
        }
        else {
            basicinfonation.setText(array.get(0).getNation());
        }
        if (array.get(0).getHeight().length() > 6) {
            basicinfoheight.setText(array.get(0).getHeight().substring(0, 6));
        } else {
            basicinfoheight.setText(array.get(0).getHeight());
        }

        if (!array.get(0).getMarriage().equals("")
                && array.get(0).getMarriage().equals("1")) {
            basicinfomarriage.setText("已婚");
        } else {
            basicinfomarriage.setText("未婚");
        }
        if (!array.get(0).getBirth().equals("")
                && array.get(0).getBirth().equals("1")) {
            basicinfobirth.setText("已育");
        } else {
            basicinfobirth.setText("未育");
        }
        basicinfoaddress.setText(array.get(0).getAddress());

        basicinfoentrytime.setText(DateUtils.parseDateDay(array.get(0)
                .getEntrytime()));

        if (!array.get(0)
                .getPositivetime().equals("暂无") && !array.get(0)
                .getEntrytime().equals("暂无")) {
            if (Integer.parseInt(array.get(0)
                    .getPositivetime()) > Integer.parseInt(array.get(0)
                    .getEntrytime())) {
                basicinfopositivetime.setText(DateUtils.parseDateDay(array.get(0)
                        .getPositivetime()));
            }
            else {
                basicinfopositivetime.setText("暂无");
            }
        }
        else {
            basicinfopositivetime.setText("暂无");
        }

        imageLoader.displayImage(array.get(0).getHeadpic(), basicinfoheadpic,
                options);

        if (array.get(0).getWorkexp() != null) {
            basicinfoworkexpadapter.workexp = array.get(0).getWorkexp();
            basicinfoworkexp.setAdapter(basicinfoworkexpadapter);
        } else {
            baiscinfoworkly.setVisibility(View.GONE);
            basicinfoworkexpfy.setVisibility(View.GONE);

        }

        if (array.get(0).getEduexp() != null) {
            basicinfolearnexpadapter.eduexp = array.get(0).getEduexp();
            basicinfoeduexp.setAdapter(basicinfolearnexpadapter);
        } else {
            baiscinfolearnly.setVisibility(View.GONE);
            baiscinfolearnfy.setVisibility(View.GONE);
        }

    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        // loading.setText("加载失败...");
        CommonUtils.setNonetIcon(mActivity, loading,this);

    }

    public boolean getdata() {
        Gson gson = new Gson();
        try {
            String urlStr;
            String jsonUrlStr;
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", uid);
            urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKINGINDEX,
                    map);
            CustomLog.e("WorkUrl2", urlStr);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            CustomLog.e("UserURL", urlStr);
            basicinfo = gson.fromJson(jsonUrlStr, DanganBasicinfo.class);
            array = basicinfo.getArray();
            if (!basicinfo.getCode().equals("200")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean checkViarbleisNull() {
        boolean isNull = false;
        if (array.get(0).getAccounts() != null) {
            if (array.get(0).getHometown() != null) {
                if (array.get(0).getNation() != null) {
                    if (array.get(0).getHeight() != null) {
                        if (array.get(0).getMarriage() != null) {
                            if (array.get(0).getBirth() != null) {
                                if (array.get(0).getAddress() != null) {
                                    isNull = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return isNull;

    }

}
