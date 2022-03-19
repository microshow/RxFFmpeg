package io.microshow.rxffmpeg.app.activity;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.utils.Utils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 启动页
 * Created by Super on 2019/12/6.
 */
public class LaunchActivity extends BaseActivity {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_launch);

        gotoMainAct();
    }

    private void gotoMainAct() {
        mCompositeDisposable.add(Flowable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.fixInputMethodManagerLeak(this);
        mCompositeDisposable.clear();
    }

}
