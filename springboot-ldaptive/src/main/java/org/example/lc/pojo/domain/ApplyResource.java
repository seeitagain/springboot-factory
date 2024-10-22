package org.example.lc.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("资源申请信息封装类")
public class ApplyResource implements Serializable {
    private static final long serialVersionUID = 4757361823176562386L;

    @ApiModelProperty(value = "用户信息", required = true)
    @NotNull
    private ApplyInfo info;

    @ApiModelProperty(value = "系统编码，枚举值：git、jenkins、ldap", required = true)
    @NotNull
    private List<String> systemCodes;
}
