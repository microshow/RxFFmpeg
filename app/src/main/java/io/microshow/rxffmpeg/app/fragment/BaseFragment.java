package io.microshow.rxffmpeg.app.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import io.microshow.rxffmpeg.app.utils.Utils;

/**
 * Created by Super on 2019/12/7.
 */
public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment {

    protected V binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, initContentView(), container, false);
            initData();
        }
        ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
        if (parent != null) {
            parent.removeView(binding.getRoot());
        }
        return binding.getRoot();
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView();

    public abstract void initData();

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 获取当前类名(没有包名)
     */
    private String getFragmentName(){
        return getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), getFragmentName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(getActivity(), getFragmentName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
        Utils.fixInputMethodManagerLeak(getContext());
    }

}
