package example.leo.com.androiddemo.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import example.leo.com.androiddemo.R;
import example.leo.com.androiddemo.bean.Video;
import example.leo.com.androiddemo.utils.FileManager;
import example.leo.com.androiddemo.utils.ToastUtil;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: MultimediaActivity
 * @Description: 多媒体
 * @Author: wanglintao
 * @CreateDate: 2019/1/4 10:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/4 10:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MultimediaActivity extends Activity implements View.OnClickListener {
    private Button btnActivityMultimediaCamera;
    private Button btnActivityMultimediaRecorderstart;
    private Button btnActivityMultimediaRecorderplay;
    private Button btnActivityMultimediaVideoplay;
    //线程操作
    private ExecutorService mExecutorService;
    //录音API
    private MediaRecorder mMediaRecorder;
    //录音开始时间与结束时间
    private long startTime, endTime;
    //录音所保存的文件
    private File mAudioFile;
    //录音文件保存位置
    private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/";
    //当前是否正在播放
    private volatile boolean isPlaying;
    //播放音频文件API
    private MediaPlayer mediaPlayer;
    VideoView videoView;
    private FileManager fileManager;
    private List<Video> videoList;

    //使用Handler更新UI线程
    private Handler mHandler = new Handler() {
        ToastUtil toastUtil;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //录音成功
                    toastUtil = new ToastUtil(MultimediaActivity.this, R.layout.toast_center, "录音成功");
                    toastUtil.show();
                    break;
                //录音失败
                case 2:
                    toastUtil = new ToastUtil(MultimediaActivity.this, R.layout.toast_center, getString(R.string.record_fail));
                    toastUtil.show();
                    break;
                //录音时间太短
                case 3:
                    toastUtil = new ToastUtil(MultimediaActivity.this, R.layout.toast_center, getString(R.string.time_too_short));
                    toastUtil.show();
                    break;
                case 4:
                    toastUtil = new ToastUtil(MultimediaActivity.this, R.layout.toast_center, getString(R.string.play_over));
                    toastUtil.show();
                    break;
                case 5:
                    toastUtil = new ToastUtil(MultimediaActivity.this, R.layout.toast_center, getString(R.string.play_error));
                    toastUtil.show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);
        //录音及播放要使用单线程操作
        mExecutorService = Executors.newSingleThreadExecutor();
        init();
        setOnclick();
        btnActivityMultimediaRecorderstart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下操作
                    case MotionEvent.ACTION_DOWN:
                        //安卓6.0以上录音相应权限处理
                        if (Build.VERSION.SDK_INT > 22) {
                            permissionForM();
                        } else {
                            startRecord();
                        }
                        break;
                    //松开操作
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        stopRecord();
                        break;
                }
                //对OnTouch事件做了处理，返回true
                return true;
            }
        });
    }

    private void init() {
        btnActivityMultimediaCamera = findViewById(R.id.btn_activity_multimedia_camera);
        btnActivityMultimediaRecorderstart = findViewById(R.id.btn_activity_multimedia_recorderstart);
        btnActivityMultimediaRecorderplay = findViewById(R.id.btn_activity_multimedia_recorderplay);
        btnActivityMultimediaVideoplay = findViewById(R.id.btn_activity_multimedia_videoplay);
        videoView = findViewById(R.id.videoView);
        fileManager = FileManager.getInstance(this);
        videoList = fileManager.getVideos();
    }

    private void setOnclick() {
        btnActivityMultimediaCamera.setOnClickListener(this);
        btnActivityMultimediaRecorderstart.setOnClickListener(this);
        btnActivityMultimediaRecorderplay.setOnClickListener(this);
        btnActivityMultimediaVideoplay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //拍照
            case R.id.btn_activity_multimedia_camera:
                Intent intent = new Intent(this, VideoActivity.class);
                startActivity(intent);
                break;
            //开始录音
            case R.id.btn_activity_multimedia_recorderstart:
                break;
            //录音播放
            case R.id.btn_activity_multimedia_recorderplay:
                playAudio(mAudioFile);
                break;
            //视频播放
            case R.id.btn_activity_multimedia_videoplay:
                if (videoList.size() > 0) {
//                    String videoUrl = Environment.getExternalStorageDirectory().getPath()+"/fl1234.mp4" ;
                    String videoUrl = videoList.get(0).getPath();
                    //设置视频控制器
                    videoView.setMediaController(new MediaController(this));
                    //播放完成回调
                    videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
                    //设置视频路径
                    videoView.setVideoPath(videoUrl);
                    //开始播放视频
                    videoView.start();
                } else {
                    ToastUtil.show(this, "没有视频文件");
                }
                break;
        }
    }


    /*******6.0以上版本手机权限处理***************************/
    /**
     * @description 兼容手机6.0权限管理
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            startRecord();
        }
    }

    /**
     * @description 开始进行录音
     */
    private void startRecord() {
        btnActivityMultimediaRecorderstart.setText(R.string.stop_by_up);
        //异步任务执行录音操作
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //播放前释放资源
                releaseRecorder();
                //执行录音操作
                recordOperation();
            }
        });
    }

    /**
     * @description 结束录音操作
     */
    private void stopRecord() {
        btnActivityMultimediaRecorderstart.setText(R.string.start_record);
        //停止录音
        mMediaRecorder.stop();
        //记录停止时间
        endTime = System.currentTimeMillis();
        //录音时间处理，比如只有大于3秒的录音才算成功
        int time = (int) ((endTime - startTime) / 1000);
        if (time >= 3) {
            //录音成功,发Message
            mHandler.sendEmptyMessage(1);
        } else {
            mAudioFile = null;
            mHandler.sendEmptyMessage(3);
        }
        //录音完成释放资源
        releaseRecorder();
    }

    /**
     * @description 录音操作
     */
    private void recordOperation() {
        //创建MediaRecorder对象
        mMediaRecorder = new MediaRecorder();
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mAudioFile = new File(mFilePath + "testRecorder" + ".m4a");
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        try {
            //创建文件
            mAudioFile.createNewFile();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mMediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置编码频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //记录开始录音时间
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            recordFail();
        }
    }

    /**
     * @description 释放录音相关资源
     */
    private void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * @description 录音失败处理
     */
    private void recordFail() {
        mAudioFile = null;
        mHandler.sendEmptyMessage(2);
    }

    /**
     * @description 播放音频
     */
    private void playAudio(final File mFile) {
        if (null != mFile && !isPlaying) {
            isPlaying = true;
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    startPlay(mFile);
                }
            });
        }
    }

    /**
     * @description 开始播放音频文件
     */
    private void startPlay(File mFile) {
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            try {
                mediaPlayer.setDataSource(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }
    }

    /**
     * @description 停止播放或播放失败处理
     * @author ldm
     * @time 2017/2/9 16:58
     */
    private void playEndOrFail(boolean isEnd) {
        isPlaying = false;
        if (isEnd) {
            mHandler.sendEmptyMessage(4);
        } else {
            mHandler.sendEmptyMessage(5);
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.canPause()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //页面销毁，线程要关闭
        mExecutorService.shutdownNow();
        stopPlaybackVideo();
    }

    private void stopPlaybackVideo() {
        try {
            videoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            ToastUtil.show(MultimediaActivity.this, "播放完成了");
        }
    }
}
