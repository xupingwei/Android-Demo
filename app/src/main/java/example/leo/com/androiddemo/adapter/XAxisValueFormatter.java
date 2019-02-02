package example.leo.com.androiddemo.adapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.adapter
 * @ClassName: XAxisValueFormatter
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/2/2 8:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/2 8:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class XAxisValueFormatter implements IAxisValueFormatter {
    private String[] xStrs = new String[]{"春", "夏", "秋", "冬"};

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value;
        if (position >= 4) {
            position = 0;
        }
        return xStrs[position];
    }
}
