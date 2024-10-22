package org.example.lc.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class ModuleInfo {
    String username;
    String name;
    List<Module> modules;
}
