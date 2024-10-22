package org.example.lc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "User", description = "用户")
public class User {
    @ApiModelProperty(value = "自增id", example = "")
    private Long id;

    @ApiModelProperty(value = "姓名", example = "")
    private String name;

    @ApiModelProperty(value = "邮箱", example = "")
    private String email;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

