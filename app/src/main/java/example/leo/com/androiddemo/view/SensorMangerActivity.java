package example.leo.com.androiddemo.view;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: SensorMangerActivity
 * @Description: SensorManger（传感器）
 * @Author: wanglintao
 * @CreateDate: 2019/1/4 10:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/4 10:11
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SensorMangerActivity extends Activity {
    private TextView tvActivitySensormanger,tv2ActivitySensormanger;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensormanger);
        tvActivitySensormanger = findViewById(R.id.tv_activity_sensormanger);
        tv2ActivitySensormanger = findViewById(R.id.tv2_activity_sensormanger);
        //获取SensorManager实例
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取当前设备支持的传感器列表
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        showSensor(allSensors);
    }

    /**
     * 传感器监听
     */
    SensorEventListener sensorEventListener = new SensorEventListener() {
        StringBuilder stringBuilder = new StringBuilder();
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.i("jeff","onSensorChanged event="+event.sensor.getName());
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
                    stringBuilder.append("x=" + event.values[0] + ",y=" + event.values[1] + ",z=" + event.values[2]+"\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    stringBuilder.append("x=" + event.values[0] + ",y=" + event.values[1] + ",z=" + event.values[2]+"\n");
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    stringBuilder.append("x=" + event.values[0] + ",y=" + event.values[1] + ",z=" + event.values[2]+"\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    stringBuilder.append("x=" + event.values[0] + ",y=" + event.values[1] + ",z=" + event.values[2]+"\n");
                    break;
            }
            tv2ActivitySensormanger.setText(stringBuilder.toString());
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Sensor mySensor = sensor;
            Log.i("jeff","onAccuracyChanged sensor="+sensor.getName()+",accuracy="+accuracy);
            switch (mySensor.getType()){
                case Sensor.TYPE_GRAVITY:
                    stringBuilder.append("重力传感器:"+mySensor.getName()+",accuracy="+accuracy+"\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    stringBuilder.append("方向传感器o-sensor:"+mySensor.getName()+",accuracy="+accuracy+"\n");
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    stringBuilder.append("加速度传感器:"+mySensor.getName()+",accuracy="+accuracy+"\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    stringBuilder.append("陀螺仪传感器gyro-sensor"+mySensor.getName()+",accuracy="+accuracy+"\n");
                    break;
            }
            tv2ActivitySensormanger.setText(stringBuilder.toString());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Sensor gSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor oSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Sensor gySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(sensorEventListener,gSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,oSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,gySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void showSensor(List<Sensor> allSensors) {
        StringBuilder sb = new StringBuilder();
        sb.append("当前设备支持传感器数：" + allSensors.size() + "   分别是：\n\n");
        for (Sensor s : allSensors) {
            /**
             * s.getName();//获得传感器名称
             * s.getType();//传感器种类
             * s.getVendor();//传感器提供商
             * s.getVersion();//传感器版本
             * s.getResolution();//精确度
             * s.getMaximumRange();//最大范围
             * s.getPower();//传感器使用时的耗电量
             */
            switch (s.getType()) {
                case Sensor.TYPE_GRAVITY:
                    sb.append("重力传感器(Gravity sensor)" + "\n");
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    sb.append("加速度传感器(Accelerometer sensor)" + "\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sb.append("陀螺仪传感器(Gyroscope sensor)" + "\n");
                    break;
                case Sensor.TYPE_LIGHT:
                    sb.append("光线传感器(Light sensor)" + "\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb.append("磁场传感器(Magnetic field sensor)" + "\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sb.append("方向传感器(Orientation sensor)" + "\n");
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb.append("气压传感器(Pressure sensor)" + "\n");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sb.append("距离传感器(Proximity sensor)" + "\n");
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sb.append("温度传感器(Temperature sensor)" + "\n");
                    break;
                default:
                    sb.append("其他传感器" + "\n");
                    break;
            }
            sb.append("设备名称：" + s.getName() + "\n 设备版本：" + s.getVersion() + "\n 供应商："
                    + s.getVendor() + "\n\n");
        }
        tvActivitySensormanger.setText(sb.toString());
        Log.d("TAG", "sb.toString()----:" + sb.toString());
    }
}