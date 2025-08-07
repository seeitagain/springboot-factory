package org.example.lc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.lc.pojo.FileReq;
import org.example.lc.service.SeaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Spring Boot Hello案例
 *
 * Created by bysocket on 26/09/2017.
 */
@Api(value = "HelloController", tags = {"hello 控制类"})
@Controller
public class TestController {
    @Autowired
    SeaFileService seaFileService;

    @ApiOperation("获取token")
    @RequestMapping(value = "/token",method = RequestMethod.GET)
    @ResponseBody
    public String token() {
        return seaFileService.generateToken();
    }

    @ApiOperation("查询文件")
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    @ResponseBody
    public String search(@RequestBody FileReq req) {
//        return seaFileService.search(req);
        return seaFileService.searchByCondition(req);
    }


    @ApiOperation("获取分享连接")
    @RequestMapping(value = "/shareLink",method = RequestMethod.POST)
    @ResponseBody
    public String shareLink(@RequestBody FileReq req) {
        return seaFileService.shareLink();
    }

}
