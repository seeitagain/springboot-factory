package org.example.lc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.lc.pojo.request.AddUserReq;
import org.example.lc.pojo.request.DelUserReq;
import org.example.lc.pojo.response.ResponseBean;
import org.example.lc.service.LdapService;
import org.ldaptive.LdapException;
import org.ldaptive.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "LdapController", tags = {"ldap控制类"})
@RestController
@RequestMapping("/user")
public class LdapController {

    @Autowired
    private LdapService ldapService;

    @ApiOperation("查询")
    @GetMapping("/search")
    public SearchResult search(@RequestParam String filter) throws LdapException {
        return ldapService.search(filter);
    }

    @ApiOperation("新增用户")
    @PostMapping("/add")
    public ResponseBean<Boolean> add(@Valid @RequestBody AddUserReq addUserReq) {
        try {
            ldapService.addUser(addUserReq);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ApiOperation("删除用户")
    @PostMapping("/del")
    public ResponseBean<Boolean> del(@Valid @RequestBody DelUserReq delUserReq) {
        try {
            ldapService.del(delUserReq);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}