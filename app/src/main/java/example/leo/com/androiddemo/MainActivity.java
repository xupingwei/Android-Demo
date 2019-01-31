package example.leo.com.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.MainAdapter;
import example.leo.com.androiddemo.view.AnimationActivity;
import example.leo.com.androiddemo.view.DataStoreActivity;
import example.leo.com.androiddemo.view.FragmentDemoActivity;
import example.leo.com.androiddemo.view.MapLocationActivity;
import example.leo.com.androiddemo.view.MultimediaActivity;
import example.leo.com.androiddemo.view.NetworkActivity;
import example.leo.com.androiddemo.view.SensorMangerActivity;
import example.leo.com.androiddemo.view.SystemAPIActivity;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    //数据集
    private List<String> contentData;
    private ListView lvActivityMain;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        adapter = new MainAdapter(example.leo.com.androiddemo.MainActivity.this,contentData);
        lvActivityMain.setAdapter(adapter);
        lvActivityMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //Android四大组件和Intent
                    case 0:
//                        intent = new Intent(MainActivity.this,FourComponentsActivity.class);
//                        startActivity(intent);
                        break;
                    //Fragment
                    case 1:
                        intent = new Intent(MainActivity.this,FragmentDemoActivity.class);
                        startActivity(intent);
                        break;
                    //数据存储
                    case 2:
                        intent = new Intent(example.leo.com.androiddemo.MainActivity.this,DataStoreActivity.class);
                        startActivity(intent);
                        break;
                    //网络编程
                    case 3:
                        intent = new Intent(example.leo.com.androiddemo.MainActivity.this,NetworkActivity.class);
                        startActivity(intent);
                        break;
                    //绘画动画
                    case 4:
                        intent = new Intent(example.leo.com.androiddemo.MainActivity.this,AnimationActivity.class);
                        startActivity(intent);
                        break;
                    //多媒体开发
                    case 5:
                        intent = new Intent(example.leo.com.androiddemo.MainActivity.this,MultimediaActivity.class);
                        startActivity(intent);
                        break;
                    //系统API
                    case 6:
                        intent = new Intent(MainActivity.this,SystemAPIActivity.class);
                        startActivity(intent);
                        break;
                    //地图定位
                    case 7:
                        intent = new Intent(example.leo.com.androiddemo.MainActivity.this,MapLocationActivity.class);
                        startActivity(intent);
                        break;
                    //SensorManger
                    case 8:
                        intent = new Intent(example.leo.com.androiddemo.MainActivity.this,SensorMangerActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

   private void initData(){
       lvActivityMain = findViewById(R.id.lv_activity_main);
       contentData = new ArrayList<>();
       contentData.add("Android四大组件和Intent");
       contentData.add("Fragment");
       contentData.add("数据存储");
       contentData.add("网络编程");
       contentData.add("绘画动画");
       contentData.add("多媒体开发");
       contentData.add("系统API");
       contentData.add("地图定位");
       contentData.add("SensorManger");
    }
}
