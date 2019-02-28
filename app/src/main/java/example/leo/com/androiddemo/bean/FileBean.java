package example.leo.com.androiddemo.bean;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.bean
 * @ClassName: FileBean
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/2/27 18:15
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/27 18:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FileBean {
    /** 文件的路径*/
    public String path;
    /**文件图片资源的id，drawable或mipmap文件中已经存放doc、xml、xls等文件的图片*/
    public int iconId;

    public FileBean(String path, int iconId) {
        this.path = path;
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "path='" + path + '\'' +
                ", iconId=" + iconId +
                '}';
    }
}
