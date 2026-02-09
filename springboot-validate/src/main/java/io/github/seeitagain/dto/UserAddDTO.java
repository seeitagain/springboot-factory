package io.github.seeitagain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserAddDTO {

    /**
     * 账号
     */
    @NotEmpty(message = "登录账号不能为空")
    @Length(min = 5, max = 16, message = "账号长度为 5-16 位")
    @Pattern(regexp="^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    public String getUsername() {
        return username;
    }

    public UserAddDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserAddDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserAddDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
