package io.github.seeitagain.controller;

import io.github.seeitagain.controller.vo.UserSaveReqVO;
import io.github.seeitagain.pojo.CommonResult;
import io.github.seeitagain.service.AdminUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static io.github.seeitagain.pojo.CommonResult.success;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    private AdminUserService userService;

    @PostMapping("/create")
    public CommonResult<Integer> createUser(@RequestBody UserSaveReqVO reqVO) {
        Integer id = userService.createUser(reqVO);
        return success(id);
    }
}
