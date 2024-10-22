package org.example.lc.pojo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "删除用户")
@Data
public class DelUserReq implements Serializable {

    private static final long serialVersionUID = 5219352610963835022L;
    @ApiModelProperty(value = "用户名", example = "san.zhang001", required = true)
    private String userName;

}
