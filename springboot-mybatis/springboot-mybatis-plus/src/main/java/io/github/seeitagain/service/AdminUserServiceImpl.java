package io.github.seeitagain.service;

import cn.hutool.core.bean.BeanUtil;
import io.github.seeitagain.controller.vo.UserSaveReqVO;
import io.github.seeitagain.mybatis.dataobject.UserDO;
import io.github.seeitagain.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService{

    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createUser(UserSaveReqVO createReqVO) {
        UserDO user = BeanUtil.toBean(createReqVO, UserDO.class);
        user.setPassword("123456");
        userMapper.insert(user);
        return user.getId();
    }
}
