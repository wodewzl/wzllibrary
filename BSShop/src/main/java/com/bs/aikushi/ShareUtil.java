
package com.bs.aikushi;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

public class ShareUtil {
    //    SHARE_MEDIA.SINA,
    final static SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN_FAVORITE
            };

    public static void share(final MainActivity activity, final String img, final String title, final String content, final String url) {
        final UMShareListener umslistener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                System.out.println("=============>");
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

        final ShareAction shareAction = new ShareAction(activity);
        shareAction.setDisplayList(displaylist);
        shareAction.addButton("copy", "android_platfor", "copy", "copy");
        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == null) {
                    if (snsPlatform.mKeyword.equals("android_platfor")) {
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText("【" + title + "】 " + content + "\n" + url);


                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.showTotast("复制成功");
                            }
                        });
                    }
                } else {

                    ShareAction action = new ShareAction(activity);
                    action.setPlatform(share_media);
//                    UMImage thumb = new UMImage(activity, img);
//        UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
//        image.setThumb(thumb);
                    UMImage image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.copy));
                    UMWeb web = new UMWeb(url);
                    web.setTitle(title);//标题
//                    web.setThumb(thumb);  //缩略图
                    web.setDescription(content);//描述
                    action.withMedia(web);

                    action.setCallback(umslistener);
                    action.share();
                }
            }
        });
        shareAction.open();
    }


}
