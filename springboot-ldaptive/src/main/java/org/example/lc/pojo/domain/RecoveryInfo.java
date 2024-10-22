package org.example.lc.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("资源回收信息")
public class RecoveryInfo implements Serializable {
    private static final long serialVersionUID = 98684746256426237L;

    @ApiModelProperty(value = "用户名", example = "san.zhang001", required = true)
    @NotNull
    private String userCode;

    @NotNull
    private List<Info> infoList;

    @ApiModelProperty(value = "业务流水编号，可据此反查申请记录信息", example = "", required = true)
    @NotNull
    private String busNo;

    @ApiModel("资源信息")
    class Info {
        @ApiModelProperty(value = "模块名称", example = "nlpmp-dcp", required = true)
        @NotNull
        private String name;

        @ApiModelProperty(value = "模块url", example = "http://192.168.84.21:8080/job/nkudp-ui-rapidx", required = true)
        @NotNull
        private String url;
    }
}
