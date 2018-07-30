package com.mvp.lt.arplibs;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/*
* dummy activity to display PanicAR fragment
* */
public abstract class ARActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        if (savedInstanceState == null) {

            FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();

            fragmentTransaction .replace(R.id.container, new PanicARFragment());
            fragmentTransaction .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
