package io.github.seeitagain.dto;

import io.github.seeitagain.constants.GenderEnum;
import io.github.seeitagain.core.validator.InEnum;

import javax.validation.constraints.NotNull;

public class UserUpdateGenderDTO {
    @NotNull(message = "用户编号不能为空")
    private Integer id;

    @NotNull(message = "性别不能为空")
    @InEnum(value = GenderEnum.class, message = "性别必须是 {value}")
    private Integer gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserUpdateGenderDTO{" +
                "id=" + id +
                ", gender=" + gender +
                '}';
    }
}
