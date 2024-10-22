package org.example.lc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lc.pojo.vo.BaseVo;
import org.example.lc.pojo.vo.ModuleVo;
import org.example.lc.pojo.vo.PersonVo;
import org.example.lc.utlis.JsonUtil;

public class VoTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        String moduleVoJson = "{\"type\":\"40\",\"detail\":[{\"username\":\"san.zhang001\",\"name\":\"张三\",\"modules\":[{\"moduleName\":\"nlpmp-dcp\",\"org\":\"rdc\"},{\"moduleName\":\"nlpmp-asf-gpt\",\"org\":\"rdc\"}]},{\"username\":\"si.li002\",\"name\":\"李四\",\"modules\":[{\"moduleName\":\"decp-permission\",\"org\":\"dept1\"}]}]}";
        String personJson = "{\"type\":\"60\",\"detail\":{\"name\":\"张三\",\"age\":\"20\"}}";

        try {
            ModuleVo obj1 = objectMapper.readValue(moduleVoJson, ModuleVo.class);
//            handleObject(obj1);

            BaseVo obj2 = objectMapper.readValue(personJson, BaseVo.class);
            handleObject(obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleObject(BaseVo baseType) {
//        if (baseType instanceof ModuleVo) {
//            System.out.println(JsonUtil.serialize((ModuleVo) baseType));
//        } else
            if (baseType instanceof PersonVo) {
            System.out.println(JsonUtil.serialize((PersonVo) baseType));
        } else {
            throw new IllegalArgumentException("Unknown type: " + baseType.getClass().getName());
        }
    }


}
