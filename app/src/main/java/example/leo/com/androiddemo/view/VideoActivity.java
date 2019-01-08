package example.leo.com.androiddemo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import example.leo.com.androiddemo.R;
import example.leo.com.androiddemo.utils.VideoUtils;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: VideoActivity
 * @Description: 拍照视频
 * @Author: wanglintao
 * @CreateDate: 2019/1/4 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/4 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class VideoActivity extends Activity implements View.OnClickListener {
    private SurfaceView svActivityVideo;
    private Button btnActivityVideoCamera;
    private ImageView ivActivityVideoVideostart, ivActivityVideoVideopause;
    //计时器
    private Chronometer mRecordTime;
    private Camera camera;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mediaRecorder;
    //录像机状态标识
    private int mRecorderState;
    public static final int STATE_INIT = 0;
    public static final int STATE_RECORDING = 1;
    public static final int STATE_PAUSE = 2;
    //录制暂停时间间隔
    private long mPauseTime = 0;
    //视频路径
    private String currentVideoFilePath;
    //视频保存路径
    private String saveVideoPath = "";

    private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        svActivityVideo = findViewById(R.id.sv_activity_video);
        btnActivityVideoCamera = findViewById(R.id.btn_activity_video_camera);
        ivActivityVideoVideostart = findViewById(R.id.iv_activity_video_videostart);
        ivActivityVideoVideopause = findViewById(R.id.iv_activity_video_videopause);
        ivActivityVideoVideostart.setOnClickListener(this);
        ivActivityVideoVideopause.setOnClickListener(this);
        mRecordTime = findViewById(R.id.record_time);
        /**
         * 配置SurfaceHolder相关
         */
        //配置SurfaceHolder
        mSurfaceHolder = svActivityVideo.getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        //回调接口
        mSurfaceHolder.addCallback(mSurfaceCallBack);
//        svActivityVideo.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                //打开照相机
//                camera = Camera.open();
//                //设置Camera的角度/方向
//                camera.setDisplayOrientation(90);
//                //设置参数
//                Camera.Parameters parameters = camera.getParameters();
//                parameters.setPictureFormat(PixelFormat.JPEG);
//                parameters.set("jpeg-quality", 85);
//                camera.setParameters(parameters);
//                //将画面展示到SurfaceView
//                try {
//                    camera.setPreviewDisplay(svActivityVideo.getHolder());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //开启预览效果
//                camera.startPreview();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                releaseCamera();
//            }
//        });
        //拍照
        btnActivityVideoCamera.setOnClickListener(this);
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void takePhoto(View view) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //图片压缩
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                try {
                    FileOutputStream fos = new FileOutputStream("/mnt/sdcard/androidDemoPic/test_" + System.currentTimeMillis() + ".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
                    camera.stopPreview();
                    camera.startPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化摄像头
     *
     * @throws IOException
     */
    private void initCamera() {
        if (camera != null) {
            releaseCamera();
        }
        camera = Camera.open();
        if (camera == null) {
            Toast.makeText(this, "未能获取到相机！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //将相机与SurfaceHolder绑定
            camera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            configCameraParams();
            //启动相机预览
            camera.startPreview();
        } catch (IOException e) {
            //有的手机会因为兼容问题报错，这就需要开发者针对特定机型去做适配了
            Log.d("VideoActivity", "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 释放摄像头资源
     */
    private void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    /**
     * 开始录制视频
     */
    public boolean startRecord() {
        initCamera();
        //录制视频前必须先解锁Camera
        camera.unlock();
        configMediaRecorder();
        try {
            //开始录制
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 停止录制视频
     */
    public void stopRecord() {
        // 设置后不会崩
        mediaRecorder.setOnErrorListener(null);
        mediaRecorder.setPreviewDisplay(null);
        //停止录制
        mediaRecorder.stop();
        mediaRecorder.reset();
        //释放资源
        mediaRecorder.release();
        mediaRecorder = null;
    }

    /**
     * 设置摄像头为竖屏
     *
     * @author lip
     * @date 2015-3-16
     */
    private void configCameraParams() {
        Camera.Parameters params = camera.getParameters();
        //设置相机的横竖屏(竖屏需要旋转90°)
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            //设置摄像头旋转90度
            camera.setDisplayOrientation(90);
        } else {
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
        }
        //设置聚焦模式
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //缩短Recording启动时间
        params.setRecordingHint(true);
        //影像稳定能力
        if (params.isVideoStabilizationSupported())
            params.setVideoStabilization(true);
        camera.setParameters(params);
    }

    /**
     * 配置MediaRecorder()
     */
    private void configMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setOnErrorListener(OnErrorListener);
        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 2 * 1024 * 1024) {
            mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        } else {
            mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
        }
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mediaRecorder.setOrientationHint(90);
        //设置录像的分辨率
        mediaRecorder.setVideoSize(352, 288);
        //设置录像视频输出地址
        mediaRecorder.setOutputFile(currentVideoFilePath);
    }

    /**
     * 合并录像视频方法
     */
    private void mergeRecordVideoFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] str = new String[]{saveVideoPath, currentVideoFilePath};
                    //将2个视频文件合并到 append.mp4文件下
                    VideoUtils.appendVideo(VideoActivity.this, getSDPath(VideoActivity.this) + "append.mp4", str);
                    File reName = new File(saveVideoPath);
                    File f = new File(getSDPath(VideoActivity.this) + "append.mp4");
                    //再将合成的append.mp4视频文件 移动到 saveVideoPath 路径下
                    f.renameTo(reName);
                    if (reName.exists()) {
                        f.delete();
                        new File(currentVideoFilePath).delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 创建视频文件保存路径
     */
    public static String getSDPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(context, "请查看您的SD卡是否存在！", Toast.LENGTH_SHORT).show();
            return null;
        }
        File sdDir = Environment.getExternalStorageDirectory();
        File eis = new File(sdDir.toString() + "/RecordVideo/");
        if (!eis.exists()) {
            eis.mkdir();
        }
        return sdDir.toString() + "/RecordVideo/";
    }

    /**
     * 创建视频名称
     * @return
     */
    private String getVideoName() {
        return "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
    }

    /**
     * 点击中间按钮，执行的UI更新操作
     */
    private void refreshControlUI() {
        if (mRecorderState == STATE_INIT) {
            //录像时间计时
            mRecordTime.setBase(SystemClock.elapsedRealtime());
            mRecordTime.start();
            ivActivityVideoVideostart.setImageResource(R.mipmap.recordvideo_stop);
            //1s后才能按停止录制按钮
            ivActivityVideoVideostart.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivActivityVideoVideostart.setEnabled(true);
                }
            }, 1000);
            ivActivityVideoVideopause.setVisibility(View.VISIBLE);
            ivActivityVideoVideopause.setEnabled(true);
        } else if (mRecorderState == STATE_RECORDING) {
            mPauseTime = 0;
            mRecordTime.stop();
            ivActivityVideoVideostart.setImageResource(R.mipmap.recordvideo_start);
            ivActivityVideoVideopause.setVisibility(View.GONE);
            ivActivityVideoVideopause.setEnabled(false);
        }
    }

    /**
     * 点击暂停继续按钮，执行的UI更新操作
     */
    private void refreshPauseUI() {
        if (mRecorderState == STATE_RECORDING) {
            ivActivityVideoVideopause.setImageResource(R.mipmap.control_play);
            mPauseTime = SystemClock.elapsedRealtime();
            mRecordTime.stop();
        } else if (mRecorderState == STATE_PAUSE) {
            ivActivityVideoVideopause.setImageResource(R.mipmap.control_pause);
            if (mPauseTime == 0) {
                mRecordTime.setBase(SystemClock.elapsedRealtime());
            } else {
                mRecordTime.setBase(SystemClock.elapsedRealtime() - (mPauseTime - mRecordTime.getBase()));
            }
            mRecordTime.start();
        }
    }

    private SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (mSurfaceHolder.getSurface() == null) {
                return;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            releaseCamera();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_video_camera:
                takePhoto(v);
                break;
            //录像
            case R.id.iv_activity_video_videostart:
                if (mRecorderState == STATE_INIT) {
                    if (getSDPath(getApplicationContext()) == null)
                        return;
                    //视频文件保存路径，configMediaRecorder方法中会设置
                    currentVideoFilePath = getSDPath(getApplicationContext()) + getVideoName();
                    //开始录制视频
                    if (!startRecord())
                        return;
                    refreshControlUI();
                    mRecorderState = STATE_RECORDING;
                } else if (mRecorderState == STATE_RECORDING) {
                    //停止视频录制
                    stopRecord();
                    //先给Camera加锁后再释放相机
                    camera.lock();
                    releaseCamera();
                    refreshControlUI();
                    //判断是否进行视频合并
                    if ("".equals(saveVideoPath)) {
                        saveVideoPath = currentVideoFilePath;
                    } else {
                        mergeRecordVideoFile();
                    }
                    mRecorderState = STATE_INIT;
                    //延迟一秒跳转到播放器，（确保视频合并完成后跳转） TODO 具体的逻辑可根据自己的使用场景跳转
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(VideoActivity.this, PlayVideoActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("videoPath", saveVideoPath);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }, 1000);
                } else if (mRecorderState == STATE_PAUSE) {
                    //代表视频暂停录制时，点击中心按钮
//                    Intent intent = new Intent(VideoActivity.this, PlayVideoActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("videoPath", saveVideoPath);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    finish();
                }
                break;
            //暂停
            case R.id.iv_activity_video_videopause:
                if (mRecorderState == STATE_RECORDING) {
                    //正在录制的视频，点击后暂停
                    //取消自动对焦
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success)
                                VideoActivity.this.camera.cancelAutoFocus();
                        }
                    });
                    stopRecord();
                    refreshPauseUI();
                    //判断是否进行视频合并
                    if ("".equals(saveVideoPath)) {
                        saveVideoPath = currentVideoFilePath;
                    } else {
                        mergeRecordVideoFile();
                    }
                    mRecorderState = STATE_PAUSE;
                } else if (mRecorderState == STATE_PAUSE) {
                    if (getSDPath(getApplicationContext()) == null)
                        return;
                    //视频文件保存路径，configMediaRecorder方法中会设置
                    currentVideoFilePath = getSDPath(getApplicationContext()) + getVideoName();
                    //继续视频录制
                    if (!startRecord()) {
                        return;
                    }
                    refreshPauseUI();
                    mRecorderState = STATE_RECORDING;
                }
                break;
        }
    }
}
