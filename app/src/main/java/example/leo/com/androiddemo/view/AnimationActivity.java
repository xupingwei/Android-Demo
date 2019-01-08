package example.leo.com.androiddemo.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: AnimationActivity
 * @Description: 绘画动画
 * @Author: wanglintao
 * @CreateDate: 2019/1/4 10:09
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/4 10:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AnimationActivity extends Activity {
    private static String picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546580255522&di=61547e881edce755d95a2a13b8e0d8aa&imgtype=0&src=http%3A%2F%2Fi1.bbs.fd.zol-img.com.cn%2Fg5%2FM00%2F0A%2F05%2FChMkJlnAEdiIEU80AAWJwRiWPjMAAgkMAGma0cABYnZ757.jpg";
    private ImageView ivActivityAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        init();
        //绑定
        RequestManager requestManager = Glide.with(this);
        /**
         * 加载本地图片
         * thumbnail 缩略图
         */
//        requestManager.load(R.mipmap.timg).thumbnail(0.3f).into(ivActivityAnimation);
        //设置动画
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(1500).setCrossFadeEnabled(true).build();
        requestManager.load(R.mipmap.timg).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(ivActivityAnimation);
    }

    private void init(){
        ivActivityAnimation = findViewById(R.id.iv_activity_animation);
    }
}
