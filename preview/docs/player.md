
# RxFFmpegPlayer

>自研RxFFmpegPlayer播放器内核基于(FFmpeg OpenGL OpenSL)；
 主打轻量级、高效、低功耗，播放本地视频秒开等特色；
 涵盖播放器基本的 播放、暂停、快进、快退、音量控制、声道控制、循环播放、播放进度、加载状态等功能；
 支持播放本地视频文件、在线视频、直播流(hls、rtmp、m3u8…)等；
 项目里已集成OpenSSL所以也支持Https访问；
 视频画面支持双指缩放、旋转、移动等手势操作;


# 特色功能

* **自研RxFFmpegPlayer播放器内核基于(FFmpeg OpenGL OpenSL)**

* **主打轻量级、高效、低功耗，播放本地视频秒开等特色**

* **涵盖播放器基本的 播放、暂停、快进、快退、音量控制、声道控制、循环播放、播放进度、加载状态等功能**

* **支持播放本地视频文件、在线视频、直播流(hls、rtmp、m3u8…)等**

* **项目里已集成OpenSSL所以也支持Https访问**

* **视频画面支持双指缩放、旋转、移动等手势操作**


# 使用方式

```java

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
           binding.editText.setText("/storage/emulated/0/1/1.mp4");
           binding.button.setOnClickListener(this);

           this.mPlayerView = binding.mPlayerView;

           //设置控制层容器 和 视频尺寸适配模式
           mPlayerView.setController(new RxFFmpegPlayerControllerImpl(getActivity()), MeasureHelper.FitModel.FM_DEFAULT);

           //播放
           mPlayerView.play(binding.editText.getText().toString(), true);

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
```

## 音量控制

* 场景：进入页面 **默认开启静音**

  * mPlayerView.setVolume(0); //该方法需要在mPlayerView.play(xxx, xxx);之前调用


* 设置声道

  * mPlayerView.setMuteSolo(int mute); //设置声道：0立体声；1左声道；2右声道


# ScreenShot

<img src="/preview/icon/screen_player.gif" alt="图-1：screen_player.gif" width="240px"></img>


## 性能/CPU/内存使用

<img src="/preview/icon/screen_player_cpu.png" alt="图-1：screen_player_cpu.png"></img>

