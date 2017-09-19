
package com.wuzhanglong.library.utils;

import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wuzhanglong.library.R;
import com.wuzhanglong.library.activity.BaseActivity;

public class ShareUtil {
//    SHARE_MEDIA.SINA, ,SHARE_MEDIA.WEIXIN_FAVORITE收藏
    final static SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
    {
            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
    };

    public static void share(final BaseActivity activity, String img, final String title, final String content, final String url) {
        final UMShareListener umslistener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(activity, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(activity, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            }
        };

        ShareAction shareAction= new ShareAction(activity);
//        UMImage thumb =  new UMImage(activity,img);
//        UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
//        image.setThumb(thumb);
//        UMImage image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.copy));

        final UMImage image;
        if (!"".equals(img) && img != null) {
            image = new UMImage(activity, img);
        } else {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher));
        }


        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);//描述
        shareAction.withMedia(web);
        shareAction.setDisplayList(displaylist);
        shareAction.setCallback(umslistener);
        shareAction.open();
    }


}
