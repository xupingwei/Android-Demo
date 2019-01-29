package example.leo.com.androiddemo.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.IOException;

import example.leo.com.androiddemo.R;
import example.leo.com.androiddemo.bean.ResultMessage;
import example.leo.com.androiddemo.bean.User;
import example.leo.com.androiddemo.utils.JsonUitl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: NetworkActivity
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/23 13:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/23 13:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NetworkActivity extends Activity {
    private TextView tvActivityNetwork;
    private static final String url = "http://192.168.100.110:8080/user/getUserInfo?userName=";
    private static final String parameter = "leo";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvActivityNetwork.setText(msg.obj.toString());
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        tvActivityNetwork = findViewById(R.id.tv_activity_network);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserInfo();
            }
        }).start();
    }

    private void getUserInfo() {
        //创建cliect对象
        OkHttpClient cliect = new OkHttpClient();
        //创建一个网络请求
        Request request = new Request.Builder()
                .url(url + parameter)
                .build();
        //创建网络请求的一个操作类
        Call call = cliect.newCall(request);
        try {
            //返回Response
            //同步请求execute()
            Response re = call.execute();
            String s = re.body().string();
            ResultMessage resultMessage = (ResultMessage) JsonUitl.stringToObject(s, ResultMessage.class);
            System.out.println(resultMessage.getData().toString());
            String userStr = JsonUitl.objectToString(resultMessage.getData());
            User user = (User) JsonUitl.stringToObject(userStr, User.class);
            if(user!=null) {
                System.out.println(user.getRegistdate());
            }
            Message message = new Message();
            message.obj = s;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
