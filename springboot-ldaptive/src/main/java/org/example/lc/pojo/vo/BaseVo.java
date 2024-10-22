package org.example.lc.pojo.vo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ModuleVo.class, name = "40"),
        @JsonSubTypes.Type(value = PersonVo.class, name = "60")
})
@Data
public abstract class BaseVo {
    public String type;
}
