package org.example.lc.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class ModuleVo {
    public String type;
    public List<ModuleInfo> detail;
}
