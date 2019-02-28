package example.leo.com.androiddemo.bean;

/**
 * @ProjectName: Android-Demo
 * @Package: example.leo.com.androiddemo.bean
 * @ClassName: Video
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/2/27 18:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/27 18:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Video {
    private int id = 0;
    private String path = null;
    private String name = null;
    private String resolution = null;// 分辨率
    private long size = 0;
    private long date = 0;
    private long duration = 0;

    public Video(int id, String path, String name, String resolution, long size, long date, long duration) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
