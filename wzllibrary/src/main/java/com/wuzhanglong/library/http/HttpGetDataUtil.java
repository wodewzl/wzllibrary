package com.wuzhanglong.library.http;

import android.util.Log;

import com.google.gson.Gson;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.cache.ACache;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.StringUtils;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ${Wuzhanglong} on 2017/9/12.
 */

public class HttpGetDataUtil {
    public static <T> void get(final BaseActivity activity, final String url, final Map<String, Object> params, final Class<T> className) {
        final Gson gson = new Gson();
        final String allUrl = BaseConstant.DOMAIN_NAME + url;
        final String cacheStr = ACache.get(activity).getAsString(allUrl + params.toString());
        if (className != null) {
            final BaseVO vo = (BaseVO) gson.fromJson(cacheStr, className);
            if (vo != null) {
                Observable.create(new Observable.OnSubscribe<BaseVO>() {
                    @Override
                    public void call(Subscriber<? super BaseVO> subscriber) {
                        subscriber.onNext(vo);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseVO>() {
                    @Override
                    public void call(BaseVO baseVO) {
                        activity.baseHasData(vo);
                    }
                });

                if (!HttpUtils.isNetworkAvailable(activity)) {
                    return;
                }
            }
        }

        Log.i("get_url", allUrl + params.toString());

        new Novate.Builder(activity)
                .baseUrl(BaseConstant.DOMAIN_NAME)
                .build()
                .rxGet(url, params, new RxStringCallback() {
                    @Override
                    public void onNext(Object o, String s) {
                        String result = new String(s);
                        if (result.equals(cacheStr) || className == null) {
                            return;
                        }
                        BaseVO baseVO = (BaseVO) gson.fromJson(result, className);
                        if ("200".equals(baseVO.getCode())) {
                            activity.baseHasData(baseVO);
                            if (!StringUtils.isEmpty(result)) {
                                ACache.get(activity).put(allUrl + params.toString(), result, 60 * 60 * 24);
                            }
                        } else if ("400".equals(baseVO.getCode())) {
                            activity.baseNoData(baseVO);
                        } else if ("600".equals(baseVO.getCode())) {
                            activity.baseNoNet();
                        } else if ("300".equals(baseVO.getCode())) {
                            activity.show();
                        }
                    }

                    @Override
                    public void onError(Object o, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Object o, Throwable throwable) {

                    }
                });
    }

    public static <T> void post(final BaseActivity activity, final String url, final Map<String, Object> params, final Class<T> className, final PostCallback postCallback) {

        final Gson gson = new Gson();
//        final String allUrl = BaseConstant.DOMAIN_NAME + url;
        new Novate.Builder(activity)
                .baseUrl(BaseConstant.DOMAIN_NAME)
                .build().rxPost(url, params, new RxStringCallback() {


            @Override
            public void onError(Object o, Throwable throwable) {

            }

            @Override
            public void onCancel(Object o, Throwable throwable) {

            }

            @Override
            public void onNext(Object o, String s) {

                BaseVO vo = (BaseVO) gson.fromJson(s, BaseVO.class);
                if ("200".equals(vo.getCode())) {
                    postCallback.success(vo);
                } else {
                    activity.showCustomToast(vo.getInfo());
                }
            }
        });
    }

}
