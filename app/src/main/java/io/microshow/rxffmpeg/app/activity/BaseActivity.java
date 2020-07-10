package io.microshow.rxffmpeg.app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.microshow.rxffmpeg.app.utils.UmengHelper;

/**
 * 基础 Activity
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengHelper.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengHelper.onPause(this);
    }

}
