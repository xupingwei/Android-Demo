package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: AndroidDemo
 * @Package: adapter
 * @ClassName: MainAdapter
 * @Description: mainactivity适配adapter
 * @Author: wanglintao
 * @CreateDate: 2019/1/3 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/3 15:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MainAdapter extends BaseAdapter {
    //数据集
    private List<String> contentData;
    //上下文对象
    private Context mContext;

    public MainAdapter(Context mContext,List<String> contentData){
        this.mContext = mContext;
        this.contentData = contentData;
    }
    @Override
    public int getCount() {
        return contentData.size();
    }

    @Override
    public Object getItem(int position) {
        return contentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_main,null);
            viewHolder.tvAdapterItemMain = convertView.findViewById(R.id.tv_adapter_item_main);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvAdapterItemMain.setText(contentData.get(position));
        return convertView;
    }

    class ViewHolder{
        private TextView tvAdapterItemMain;
    }
}