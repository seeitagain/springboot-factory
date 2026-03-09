package io.github.seeitagain.service;


import io.github.seeitagain.controller.vo.UserSaveReqVO;

/**
 * 后台用户 Service 接口
 *
 * @author lc
 */
public interface AdminUserService {

    /**
     * 创建用户
     *
     * @param createReqVO 用户信息
     * @return 用户编号
     */
    Integer createUser(UserSaveReqVO createReqVO);
}
