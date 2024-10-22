package org.example.lc.service;

import org.example.lc.pojo.domain.ApplyInfo;
import org.example.lc.pojo.domain.QuitInfo;
import org.example.lc.pojo.domain.RecoveryInfo;
import org.example.lc.pojo.response.ResponseBean;

public interface ResourceService {
    /**
     * 资源申请
     *
     * @param info
     * @return
     */
    ResponseBean<Boolean> apply(ApplyInfo info);

    /**
     * 资源回收
     *
     * @param info
     * @return
     */
    ResponseBean<Boolean> recovery(RecoveryInfo info);

    /**
     * 注销
     * @param info
     * @return
     */
    ResponseBean<Boolean> cancel(QuitInfo info);

    /**
     * 资源类别编码
     *
     * @return
     */
    String resourceCode();
}
