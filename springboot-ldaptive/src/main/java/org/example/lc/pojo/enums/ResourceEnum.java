package org.example.lc.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ResourceEnum {
    ldap("ldap","ldap"),
    git("git","git"),
    jenkins("jenkins", "jenkins");

    @EnumValue
    private final String code;
    private final String desc;

    ResourceEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
