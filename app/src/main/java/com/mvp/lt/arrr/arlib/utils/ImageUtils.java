package com.mvp.lt.arrr.arlib.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by ${LiuTao}.
 * User: Administrator
 * Name: ARPLibs
 * functiona:
 * Date: 2019/6/27 0027
 * Time: 下午 16:12
 */
public class ImageUtils {

    /**
     * 旋转图片
     * @param bitmap
     * @param rotation
     * @Return
     */
    public static Bitmap getRotatedBitmap(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }

    /**
     * 镜像翻转图片
     * @param bitmap
     * @Return
     */
    public static Bitmap getFlipBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(bitmap.getWidth(), 0);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }
}
