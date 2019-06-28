package com.mvp.lt.arplibs;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxPermissions rxPermissions =new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Boolean value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void startARPortrait(View view) {
        Intent intent = new Intent(this, ARPortraitActivity.class);
        startActivity(intent);
    }
    public void startARLandscape(View view) {
        Intent intent = new Intent(this, ARLandscapeActivity.class);
        startActivity(intent);
    }
    public void startARAutoOrienting(View view) {
        Intent intent = new Intent(this, ARAutoOrientingActivity.class);
        startActivity(intent);
    }

    public void CameraSurfaceViewActivity(View view) {
        Intent intent = new Intent(this, CameraSurfaceViewActivity.class);
        startActivity(intent);
    }
}
