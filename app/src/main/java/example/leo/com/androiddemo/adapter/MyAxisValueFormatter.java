package example.leo.com.androiddemo.adapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.adapter
 * @ClassName: MyAxisValueFormatter
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/2/2 8:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/2 8:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyAxisValueFormatter implements IAxisValueFormatter {
    private DecimalFormat mFormat;

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.000");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " $";
    }
}
