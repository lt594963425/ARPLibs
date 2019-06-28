package com.mvp.lt.arrr.arlib.view;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mvp.lt.arrr.arlib.utils.CameraUtils;
import com.mvp.lt.arrr.arlib.utils.PSKUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * $activityName
 *
 * @author LiuTao
 * @date 2018/7/27/027
 */


public class PARCameraView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "PARCameraView";
    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;
    private int cameraId = 0;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private Camera.Size bestPreviewSize;
    private float displaySizeW = 16.0F;
    private float displaySizeH = 9.0F;
    private float resolvedAspectRatioW;
    private float resolvedAspectRatioH;
    private float parentWidth;
    private float parentHeight;
    private DisplayMetrics displaymetrics;
    public int mOrientation;

    public PARCameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.resolvedAspectRatioW = this.displaySizeW;
        this.resolvedAspectRatioH = this.displaySizeH;
        this.displaymetrics = new DisplayMetrics();
    }

    public PARCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.resolvedAspectRatioW = this.displaySizeW;
        this.resolvedAspectRatioH = this.displaySizeH;
        this.displaymetrics = new DisplayMetrics();
    }

    public PARCameraView(Context context) {
        super(context);
        this.resolvedAspectRatioW = this.displaySizeW;
        this.resolvedAspectRatioH = this.displaySizeH;
        this.displaymetrics = new DisplayMetrics();
    }

    public void onCreateView() {
        this.getHolder().addCallback(this);
        Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        try {
            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealMetrics(this.displaymetrics);
            } else {
                display.getMetrics(this.displaymetrics);
            }

            this.parentWidth = (float) this.displaymetrics.widthPixels;
            this.parentHeight = (float) this.displaymetrics.heightPixels;
        } catch (Exception var3) {
            var3.printStackTrace();
            this.parentWidth = 1280.0F;
            this.parentHeight = 1280.0F;
        }

    }

    public Point getViewSize() {
        return new Point((int) this.parentWidth, (int) this.parentHeight);
    }

    public void onResume() {
        try {
            this.camera = Camera.open(this.cameraId);
            this.startCameraPreview();
        } catch (Exception var2) {
            Log.e("PARCameraView", "Can't open camera with id " + this.cameraId, var2);
        }
    }

    public void onPause() {
        try {
            if (this.camera != null) {
                this.stopCameraPreview();
                this.camera.release();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private synchronized void startCameraPreview() {
        this.determineDisplayOrientation();
        this.setupCamera();

        try {
            this.camera.setPreviewDisplay(this.surfaceHolder);
            this.camera.startPreview();
        } catch (IOException var2) {
            Log.e("PARCameraView", "Can't start camera preview due to IOException", var2);
        } catch (NullPointerException var3) {
            var3.printStackTrace();
        }

    }

    private synchronized void stopCameraPreview() {
        try {
            this.camera.stopPreview();
        } catch (Exception var2) {
            Log.i("PARCameraView", "Exception during stopping camera preview");
        }

    }

    public void determineDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(this.cameraId, cameraInfo);
        int rotation = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case 0:
                this.resolvedAspectRatioW = this.displaySizeW;
                this.resolvedAspectRatioH = this.displaySizeH;
                this.parentWidth = (float) this.displaymetrics.widthPixels;
                this.parentHeight = (float) this.displaymetrics.heightPixels;
                degrees = 0;
                break;
            case 1:
                this.resolvedAspectRatioW = this.displaySizeH;
                this.resolvedAspectRatioH = this.displaySizeW;
                this.parentWidth = (float) this.displaymetrics.heightPixels;
                this.parentHeight = (float) this.displaymetrics.widthPixels;
                degrees = 90;
                break;
            case 2:
                this.resolvedAspectRatioW = this.displaySizeW;
                this.resolvedAspectRatioH = this.displaySizeH;
                this.parentWidth = (float) this.displaymetrics.widthPixels;
                this.parentHeight = (float) this.displaymetrics.heightPixels;
                degrees = 180;
                break;
            case 3:
                this.resolvedAspectRatioW = this.displaySizeH;
                this.resolvedAspectRatioH = this.displaySizeW;
                this.parentWidth = (float) this.displaymetrics.heightPixels;
                this.parentHeight = (float) this.displaymetrics.widthPixels;
                degrees = 270;
        }

        if (cameraInfo.facing == 1) {
            mOrientation = (cameraInfo.orientation + degrees) % 360;
            mOrientation = (360 - mOrientation) % 360;
        } else {
            mOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        if (this.camera != null) {
            this.camera.setDisplayOrientation(mOrientation);
        }

    }

    public void setupCamera() {
        if (this.camera != null) {
            Camera.Parameters parameters = this.camera.getParameters();
            parameters.setRecordingHint(true);
            this.camera.setParameters(parameters);
            setPreviewSize(camera, CameraUtils.DEFAULT_WIDTH, CameraUtils.DEFAULT_HEIGHT);
            setPictureSize(camera, CameraUtils.DEFAULT_WIDTH, CameraUtils.DEFAULT_HEIGHT);

        }

    }
    /**
     * 设置预览大小
     * @param camera
     * @param expectWidth
     * @param expectHeight
     */
    public static void setPreviewSize(Camera camera, int expectWidth, int expectHeight) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = calculatePerfectSize(parameters.getSupportedPreviewSizes(), expectWidth, expectHeight);
        parameters.setPreviewSize(size.width, size.height);
        camera.setParameters(parameters);
    }
    /**
     * 设置拍摄的照片大小
     * @param camera
     * @param expectWidth
     * @param expectHeight
     */
    public static void setPictureSize(Camera camera, int expectWidth, int expectHeight) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = calculatePerfectSize(parameters.getSupportedPictureSizes(),
                expectWidth, expectHeight);
        parameters.setPictureSize(size.width, size.height);
        camera.setParameters(parameters);
    }
    /**
     * 计算最完美的Size
     * @param sizes
     * @param expectWidth
     * @param expectHeight
     * @return
     */
    public static Camera.Size calculatePerfectSize(List<Camera.Size> sizes, int expectWidth,
                                                   int expectHeight) {
        sortList(sizes); // 根据宽度进行排序
        Camera.Size result = sizes.get(0);
        boolean widthOrHeight = false; // 判断存在宽或高相等的Size
        // 辗转计算宽高最接近的值
        for (Camera.Size size: sizes) {
            // 如果宽高相等，则直接返回
            if (size.width == expectWidth && size.height == expectHeight) {
                result = size;
                break;
            }
            // 仅仅是宽度相等，计算高度最接近的size
            if (size.width == expectWidth) {
                widthOrHeight = true;
                if (Math.abs(result.height - expectHeight)
                        > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
            // 高度相等，则计算宽度最接近的Size
            else if (size.height == expectHeight) {
                widthOrHeight = true;
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(size.width - expectWidth)) {
                    result = size;
                }
            }
            // 如果之前的查找不存在宽或高相等的情况，则计算宽度和高度都最接近的期望值的Size
            else if (!widthOrHeight) {
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(size.width - expectWidth)
                        && Math.abs(result.height - expectHeight)
                        > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
        }
        return result;
    }
    /**
     * 排序
     * @param list
     */
    private static void sortList(List<Camera.Size> list) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size pre, Camera.Size after) {
                if (pre.width > after.width) {
                    return 1;
                } else if (pre.width < after.width) {
                    return -1;
                }
                return 0;
            }
        });
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float originalWidth = (float) MeasureSpec.getSize((int)this.parentWidth);
        float originalHeight = (float) MeasureSpec.getSize((int)this.parentHeight);
//        float width = originalWidth;
//        float height = originalHeight;
//        float parentWidth = (float)((ViewGroup)this.getParent()).getMeasuredWidth();
//        float parentHeight = (float)((ViewGroup)this.getParent()).getMeasuredHeight();
//        if(originalWidth > originalHeight * this.getResolvedAspectRatio()) {
//            width = originalHeight / this.getResolvedAspectRatio() + 0.5F;
//        } else {
//            height = originalWidth * this.getResolvedAspectRatio() + 0.5F;
//        }
//
//        this.setX((parentWidth - width) * 0.5F);
//        this.setY((parentHeight - height) * 0.5F);
        this.setMeasuredDimension((int)originalHeight, (int)originalWidth);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceHolder = holder;

        try {
            this.startCameraPreview();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private float getResolvedAspectRatio() {
        Log.e("PARCameraView", "getResolvedAspectRatio,,,"+ resolvedAspectRatioW+"------"+resolvedAspectRatioH);//0.5625

        return this.resolvedAspectRatioW / this.resolvedAspectRatioH;
    }
}
