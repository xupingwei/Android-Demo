package example.leo.com.androiddemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.fragment
 * @ClassName: FindFragment
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/30 9:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/30 9:00
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FindFragment extends Fragment {
    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_find,null);
        }
        ((TextView)mView.findViewById(R.id.sTextView)).setText("查找界面");
        return mView;
    }
}
