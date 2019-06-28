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
import android.view.WindowManager;

import com.mvp.lt.arrr.arlib.utils.CameraUtils;

/**
 * Created by ${LiuTao}.
 * User: Administrator
 * Name: ARPLibs
 * functiona:
 * Date: 2019/6/27 0027
 * Time: 下午 15:48
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private SurfaceHolder mSurfaceHolder;
    public DisplayMetrics mDisplaymetrics;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mDisplaymetrics = new DisplayMetrics();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CameraUtils.openFrontalCamera(CameraUtils.DESIRED_PREVIEW_FPS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        CameraUtils.startPreviewDisplay(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraUtils.releaseCamera();
    }

    public void onResume() {
        if (mSurfaceHolder != null) {
            CameraUtils.startPreviewDisplay(mSurfaceHolder);
        }

    }

    public void onPause() {
        CameraUtils.releaseCamera();
    }

    public Point getViewSize() {
        Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getRealMetrics(this.mDisplaymetrics);
        float parentWidth = (float) this.mDisplaymetrics.widthPixels;
        float parentHeight = (float) this.mDisplaymetrics.heightPixels;
        return new Point((int) parentWidth, (int) parentHeight);
    }

    public void determineDisplayOrientation() {
        CameraUtils.determineDisplayOrientation(getContext());

    }


}
