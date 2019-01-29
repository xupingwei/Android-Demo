package example.leo.com.androiddemo.bean;

import java.util.Date;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.bean
 * @ClassName: User
 * @Description: 用户实体类
 * @Author: wanglintao
 * @CreateDate: 2019/1/23 15:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/23 15:48
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class User {
    private Integer id;

    private String userid;

    private String username;

    private String userpassword;

    private String idcard;

    private String phonenum;

    private Date birthdate;

    private Date registdate;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword == null ? null : userpassword.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getRegistdate() {
        return registdate;
    }

    public void setRegistdate(Date registdate) {
        this.registdate = registdate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}
