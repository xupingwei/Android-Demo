package custom;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: Android-Demo
 * @Package: custom
 * @ClassName: XYMarkerView
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/2/2 8:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/2 8:56
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class XYMarkerView extends MarkerView {
    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;
    private DecimalFormat format;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.000");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("x：" + xAxisValueFormatter.getFormattedValue(e.getX(), null) + "，y：" + format.format(e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
