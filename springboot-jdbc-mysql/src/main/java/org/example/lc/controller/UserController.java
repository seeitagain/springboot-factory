package org.example.lc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.lc.entity.User;
import org.example.lc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "UserController", tags = {"用户控制类"})
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取全部用户")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation("新增用户")
    @PostMapping
    public int createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
