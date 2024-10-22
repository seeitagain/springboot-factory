package org.example.lc.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("资源信息封装类")
public class RecoveryResource implements Serializable {
    private static final long serialVersionUID = 7604514161375473554L;

    @ApiModelProperty(value = "用户信息", required = true)
    @NotNull
    private RecoveryInfo info;

    @ApiModelProperty(value = "系统编码，枚举值：git、jenkins、ldap", required = true)
    @NotNull
    private List<String> systemCodes;
}
