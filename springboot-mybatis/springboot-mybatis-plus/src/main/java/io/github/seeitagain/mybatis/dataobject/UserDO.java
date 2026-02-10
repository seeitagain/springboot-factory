package io.github.seeitagain.mybatis.dataobject;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName(value = "demo_users")
public class UserDO {
    private Integer id;

    private String username;

    private String password;

    private Date createTime;

    @TableLogic
    private Integer deleted;

    public Integer getId() {
        return id;
    }

    public UserDO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDO setPassword(String password) {
        this.password = password;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public UserDO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public UserDO setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }
}
