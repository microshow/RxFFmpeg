package io.microshow.rxffmpeg.app.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import androidx.annotation.Nullable;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.databinding.FragmentMeBinding;

/**
 * 我
 * Created by Super on 2019/12/7.
 */
public class MeFragment extends BaseFragment<FragmentMeBinding> {

    private AgentWeb agentWeb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_me;
    }

    @Override
    public void initData() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.llFind, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://github.com/microshow/RxFFmpeg");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return agentWeb != null && agentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (agentWeb != null) {
            agentWeb.destroy();
        }
    }

}
