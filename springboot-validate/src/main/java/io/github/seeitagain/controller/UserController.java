package io.github.seeitagain.controller;

import io.github.seeitagain.dto.UserAddDTO;
import io.github.seeitagain.dto.UserUpdateGenderDTO;
import io.github.seeitagain.dto.UserUpdateStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
//在类上，添加 @Validated 注解，表示 UserController 是所有接口都需要进行参数校验
@Validated
public class UserController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/get")
    public void get(@RequestParam("id") @Min(value = 1L, message = "编号必须大于 0") Integer id) {
        logger.info("[get][id: {}]", id);
    }

    @PostMapping("/add")
    public void add(@Valid UserAddDTO addDTO) {
        logger.info("[add][addDTO: {}]", addDTO);
    }

    @PostMapping("/update_gender")
    public void updateGender(@Valid UserUpdateGenderDTO updateGenderDTO) {
        logger.info("[updateGender][updateGenderDTO: {}]", updateGenderDTO);
    }

    @PostMapping("/update_status_true")
    public void updateStatusTrue(@Validated(UserUpdateStatusDTO.Group01.class) UserUpdateStatusDTO updateStatusDTO) {
        logger.info("[updateStatusTrue][updateStatusDTO: {}]", updateStatusDTO);
    }

    @PostMapping("/update_status_false")
    public void updateStatusFalse(@Validated(UserUpdateStatusDTO.Group02.class) UserUpdateStatusDTO updateStatusDTO) {
        logger.info("[updateStatusFalse][updateStatusDTO: {}]", updateStatusDTO);
    }
}
