package io.microshow.rxffmpeg.app.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import androidx.annotation.Nullable;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.databinding.FragmentFindBinding;

/**
 * Created by Super on 2019/12/7.
 */
public class FindFragment extends BaseFragment<FragmentFindBinding> {

    private AgentWeb agentWeb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_find;
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return agentWeb != null && agentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }
}
