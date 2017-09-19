package com.wuzhanglong.library.utils;

/**
 * Created by Administrator on 2017/3/10.
 */

public class OpenActivityUtls {
//    // 更具类打开acitvity
//    public void openActivity(Class<?> pClass) {
//        openActivity(pClass, null, 0);
//
//    }
//
//    public void openActivityForResult(Class<?> pClass, int requestCode) {
//        openActivity(pClass, null, requestCode);
//    }
//
//    // 更具类打开acitvity,并携带参数
//    public void openActivity(Context context,Class<?> pClass, Bundle pBundle, int requestCode) {
//        Intent intent = new Intent(context, pClass);
//        if (pBundle != null) {
//            intent.putExtras(pBundle);
//        }
//        if (requestCode == 0) {
//            Log.e("openActivity","pClass"+pClass.getSimpleName().toString());
////            IntentUtils.startPreviewActivity(context, intent, 0);
//             startActivity(intent);
//        } else {
////            IntentUtils.startPreviewActivity(context, intent, requestCode);
//             startActivityForResult(intent, requestCode);
//        }
//        actityAnim();
//    }
//
//    public void openNewTaskActivity(Context context,Class<?> pClass, Bundle pBundle, int requestCode) {
//        Intent intent = new Intent(context, pClass);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        if (pBundle != null) {
//            intent.putExtras(pBundle);
//        }
//        if (requestCode == 0) {
//            IntentUtils.startPreviewActivity(this, intent, 0);
//            // startActivity(intent);
//        } else {
//            IntentUtils.startPreviewActivity(this, intent, requestCode);
//            // startActivityForResult(intent, requestCode);
//        }
//        actityAnim();
//    }
//
//    public void actityAnim() {
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//    }
}
