
package com.bs.bsims.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTSharedfilesdGroupHomeAllAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Sharedfiles;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.SharedfilesdHomeAllFragmentVO;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

/**
 * @author peck
 * @Description: 文档管理接口 1. 文档获取部门列表接口
 * @date 2015-7-4 上午10:04:41
 * @email 971371860@qq.com
 * @version V1.0
 */
public class SharedfilesdHomeAllFragment extends BaseFragment implements
        UpdateCallback, OnClickListener {
    private static final String TAG = "SharedfilesdHomeAllFragment";
    private Activity mActivity;
    private BSRefreshListView fragment_sharedfilesd_home_all_refreshlistview;

    private SharedfilesdHomeAllFragmentVO mSharedfilesdHomeAllFragmentVO;
    private EXTSharedfilesdGroupHomeAllAdapter mEXTSharedfilesdGroupHomeAllAdapter;

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void executeSuccess() {
        mEXTSharedfilesdGroupHomeAllAdapter.updateData(mSharedfilesdHomeAllFragmentVO.getArray());
        // mListView.onRefreshComplete();
        // footViewIsVisibility(mAdapter.mList);

        CustomDialog.closeProgressDialog();
    }

    @Override
    public void executeFailure() {
        // mTextView.setText("加载失败");
        if (mSharedfilesdHomeAllFragmentVO != null) {
            // mLoading.setText(mSharedfilesdHomeAllFragmentVO.getRetinfo());
            CustomToast.showShortToast(mActivity, mSharedfilesdHomeAllFragmentVO.getRetinfo());
        }  

        CustomDialog.closeProgressDialog();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.peck_allgroup_listview_refshnodelte, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        CustomDialog.showProgressDialog(mActivity);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        fragment_sharedfilesd_home_all_refreshlistview = (BSRefreshListView) view
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview);
        mEXTSharedfilesdGroupHomeAllAdapter = new EXTSharedfilesdGroupHomeAllAdapter(
                mActivity);
        fragment_sharedfilesd_home_all_refreshlistview
                .setAdapter(mEXTSharedfilesdGroupHomeAllAdapter);
    }

    public boolean getData() {
        Gson gson = new Gson();
        // http://cp.beisheng.wang/api.php/Sharedfiles/getDepList/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
        try {
            String urlStr = UrlUtil.getUrlByMap(
                    Constant4Sharedfiles.DEPLIST_PATH, null);
            String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            mSharedfilesdHomeAllFragmentVO = gson.fromJson(jsonUrlStr, SharedfilesdHomeAllFragmentVO.class);
            if (null != mSharedfilesdHomeAllFragmentVO) {
                if (Constant.RESULT_CODE.equals(mSharedfilesdHomeAllFragmentVO
                        .getCode())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
