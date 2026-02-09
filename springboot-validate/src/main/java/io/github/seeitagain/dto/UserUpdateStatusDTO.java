package io.github.seeitagain.dto;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;

public class UserUpdateStatusDTO {
    public interface Group01{}

    public interface Group02{}

    @AssertFalse(message = "状态必须为 false", groups = Group02.class)
    @AssertTrue(message = "状态必须为 true", groups = Group01.class)
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
