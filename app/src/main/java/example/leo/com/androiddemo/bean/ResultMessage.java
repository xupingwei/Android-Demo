package example.leo.com.androiddemo.bean;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.bean
 * @ClassName: ResultMessage
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/23 15:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/23 15:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ResultMessage {
    private String errcode;
    private String errmsg;
    private User data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
