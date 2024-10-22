package org.example.lc.pojo.response;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public final class ResponseBean<T> implements Serializable {
    private static final long serialVersionUID = -8313419428896543658L;
    @NotNull
    @ApiModelProperty(
            value = "返回编码",
            example = "000000",
            required = true
    )
    private String code;
    @NotNull
    @ApiModelProperty(
            value = "返回信息",
            example = "操作成功",
            required = true
    )
    private String message;
    @ApiModelProperty("返回数据")
    private T object;

    /** @deprecated */
    @Deprecated
    public ResponseBean() {
    }

    ResponseBean(@NotNull ResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    ResponseBean(@NotNull ResponseStatus status, T object) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.object = object;
    }

    ResponseBean(@NotNull ResponseBean responseBean) {
        this.code = responseBean.getCode();
        this.message = responseBean.getMessage();
    }

    ResponseBean(@NotNull ResponseBean responseBean, T object) {
        this.code = responseBean.getCode();
        this.message = responseBean.getMessage();
        this.object = object;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getObject() {
        return this.object;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObject(T object) {
        this.object = object;
    }
}