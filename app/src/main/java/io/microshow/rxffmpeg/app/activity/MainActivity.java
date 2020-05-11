package io.microshow.rxffmpeg.app.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.fragment.FindFragment;
import io.microshow.rxffmpeg.app.fragment.HomeFragment;
import io.microshow.rxffmpeg.app.databinding.ActivityMainBinding;
import io.microshow.rxffmpeg.app.fragment.MeFragment;
import io.microshow.rxffmpeg.app.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    private FragmentTabHost myTabhost;

    private String homeTabTexts[] = {"剪辑", "实验室", "我"};

    //加载的Fragment
    private Class mFragment[] = {
            HomeFragment.class, FindFragment.class, MeFragment.class
    };

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initTabHost();

        //baidu mtj-sdk 崩溃日志
        StatService.start(this);
    }

    private void initTabHost() {
        myTabhost = findViewById(android.R.id.tabhost);
        myTabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//        myTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String s) {
//            }
//        });
        //去掉分割线
        myTabhost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < homeTabTexts.length; i++) {
            //对Tab按钮添加标记和图片
            TabHost.TabSpec tabSpec = myTabhost.newTabSpec(i + "").setIndicator(getHomeTabItem(i));
            //添加Fragment
            myTabhost.addTab(tabSpec, mFragment[i], null);
        }
    }

    //获取底部导航 item
    private View getHomeTabItem(int index) {
        View view = getLayoutInflater().inflate(R.layout.home_tab_item, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.img_tab_pic);
//        imageView.setImageResource(homeTabIcons[index]);
        TextView textView = view.findViewById(R.id.img_tab_text);
        textView.setText(homeTabTexts[index]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Fragment mFragment = getSupportFragmentManager().findFragmentByTag(myTabhost.getCurrentTabTag());
        MeFragment mMeFragment = null;
        FindFragment mFindFragment = null;

        if (mFragment instanceof MeFragment) {
            mMeFragment = (MeFragment) mFragment;
        }

        if (mFragment instanceof FindFragment) {
            mFindFragment = (FindFragment) mFragment;
        }

        if (mMeFragment != null) {
            if (mMeFragment.onKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        } else if (mFindFragment != null) {
            if (mFindFragment.onKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
        if (myTabhost != null) {
            myTabhost.clearAllTabs();
            myTabhost = null;
        }
        Utils.fixInputMethodManagerLeak(this);
    }
}