package org.example.lc.pojo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "新增用户")
@Data
public class AddUserReq implements Serializable {
    private static final long serialVersionUID = -924264960096376543L;

    @ApiModelProperty(value = "用户名", example = "san.zhang001", required = true)
    private String userName;
    @ApiModelProperty(value = "密码", example = "", required = true)
    private String pwd;
    @ApiModelProperty(value = "邮箱", example = "san.zhang001@northking.net", required = true)
    private String email;

}
