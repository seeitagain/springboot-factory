package org.example.lc.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("用户信息")
public class ApplyInfo implements Serializable {
    private static final long serialVersionUID = -9033258006956394218L;

    @ApiModelProperty(value = "用户名", example = "san.zhang001", required = true)
    @NotNull
    private String userCode;

    @ApiModelProperty(value = "明文密码", example = "123456", required = true)
    @NotNull
    private String password;

    @ApiModelProperty(value = "邮箱", example = "san.zhang001@northking.net", required = true)
    @NotNull
    private String mail;

    @ApiModelProperty(value = "业务流水编号，可据此反查申请记录信息", example = "", required = true)
    @NotNull
    private String busNo;
}
