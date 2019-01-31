package example.leo.com.androiddemo.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: SystemAPIActivity
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/30 14:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/30 14:06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SystemAPIActivity extends Activity {
    private final static String apiStr = "http://www.android-doc.com/reference/packages.html";
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemapi);
        web = findViewById(R.id.web);
        web.loadUrl(apiStr);
    }
}
