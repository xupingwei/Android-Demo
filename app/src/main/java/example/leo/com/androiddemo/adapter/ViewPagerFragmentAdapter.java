package example.leo.com.androiddemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.adapter
 * @ClassName: ViewPagerFragmentAdapter
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/30 9:12
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/30 9:12
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList = new ArrayList<Fragment>();

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }
}
