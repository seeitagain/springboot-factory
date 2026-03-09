package io.github.seeitagain.controller.vo;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

@Data
public class UserSaveReqVO {

    private Integer id;

    private String username;

    private String nickname;

    private String remark;

    private Long deptId;

    private Set<Long> postIds;

    private String email;

    private String mobile;

    private Integer sex;

    private String avatar;

    // ========== 仅【创建】时，需要传递的字段 ==========

    private String password;

    @JsonIgnore
    public boolean isPasswordValid() {
        return id != null // 修改时，不需要传递
                || (ObjectUtil.isAllNotEmpty(password)); // 新增时，必须都传递 password
    }

}
