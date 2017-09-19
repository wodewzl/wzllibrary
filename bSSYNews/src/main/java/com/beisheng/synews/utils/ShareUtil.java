
package com.beisheng.synews.utils;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.beisheng.base.R;
import com.beisheng.base.utils.DialogUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareUtil {
    final static SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
    {
            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
    };

    public static void share(final Activity activity, String img, String title, String content, String url) {
        UMShareListener umslistener = new UMShareListener() {
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

        Config.dialog = DialogUtil.createLoadingDialog(activity);
        final UMImage image;
        if (!"".equals(img) && img != null) {
            image = new UMImage(activity, img);
        } else {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher));
        }
        ShareAction shareAction = new ShareAction(activity).setDisplayList(displaylist);
        if ("".equals(content) || content == null)
            content = "分享自十堰头条";
        shareAction.withText(content);
        if (!"".equals(title) && title != null)
            shareAction.withTitle(title);
        if (!"".equals(url) && url != null)
            shareAction.withTargetUrl(url);
        shareAction.withMedia(image);
        shareAction.setListenerList(umslistener);
        shareAction.open();
    }
}
