package example.leo.com.androiddemo.bean;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.bean
 * @ClassName: ImgFolderBean
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/2/27 18:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/27 18:23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImgFolderBean {
    /**
     * 当前文件夹的路径
     */
    private String dir;
    /**
     * 第一张图片的路径
     */
    private String fistImgPath;
    /**
     * 文件夹名
     */
    private String name;
    /**
     * 文件夹中图片的数量
     */
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndex = dir.lastIndexOf("/");
        this.name = dir.substring(lastIndex + 1);
    }

    public String getFistImgPath() {
        return fistImgPath;
    }

    public void setFistImgPath(String fistImgPath) {
        this.fistImgPath = fistImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ImgFolderBean() {
    }

    public ImgFolderBean(String dir, String fistImgPath, String name, int count) {
        this.dir = dir;
        this.fistImgPath = fistImgPath;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImgFolderBean{" +
                "dir='" + dir + '\'' +
                ", fistImgPath='" + fistImgPath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
