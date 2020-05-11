package io.microshow.rxffmpeg.app.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.databinding.FragmentFindBinding;
import io.microshow.rxffmpeg.player.MeasureHelper;
import io.microshow.rxffmpeg.player.RxFFmpegPlayerControllerImpl;
import io.microshow.rxffmpeg.player.RxFFmpegPlayerView;

/**
 * 发现
 * Created by Super on 2019/12/7.
 */
public class FindFragment extends BaseFragment<FragmentFindBinding> implements View.OnClickListener {

    private RxFFmpegPlayerView mPlayerView;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            if (!TextUtils.isEmpty(binding.editText.getText().toString())) {
                mPlayerView.play(binding.editText.getText().toString(), false);
            }
        }
    }

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
        //设置播放url
        binding.editText.setText("https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200fb40000bnvha302saj67106apf0&ratio=720p&line=0");
//        binding.editText.setText("https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0300f7a0000bluavcsk9po2b28m1il0&ratio=720p&line=0");
//        binding.editText.setText("/storage/emulated/0/1/1.mp4");
        binding.button.setOnClickListener(this);

        this.mPlayerView = binding.mPlayerView;

        //设置控制层容器 和 视频尺寸适配模式
        mPlayerView.setController(new RxFFmpegPlayerControllerImpl(getActivity()), MeasureHelper.FitModel.FM_DEFAULT);

        //播放
        mPlayerView.play(binding.editText.getText().toString(), true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.isFullScreenModel()) {//当前处于全屏模式，这里需要退出全屏
            mPlayerView.switchScreen();//退出全屏
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //恢复播放
        mPlayerView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //暂停视频
        mPlayerView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁播放器
        mPlayerView.release();
    }

}
