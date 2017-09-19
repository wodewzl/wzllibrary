package com.wuzhanglong.library.utils;

import android.Manifest;

import com.wuzhanglong.library.activity.BaseActivity;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ${Wuzhanglong} on 2017/6/13.
 */
public class PhotoSelectUtil {
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
//    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    public static void selectPhoto(BaseActivity activity,int maxCount,String file) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(activity, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(FileUtil.getSaveFilePath(activity,file));
            activity.startActivityForResult(BGAPhotoPickerActivity.newIntent(activity, takePhotoDir, maxCount, null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(activity, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }
}
