package org.example.lc.service.resource;

import lombok.extern.slf4j.Slf4j;
import org.example.lc.pojo.domain.ApplyInfo;
import org.example.lc.pojo.domain.QuitInfo;
import org.example.lc.pojo.domain.RecoveryInfo;
import org.example.lc.pojo.enums.ResourceEnum;
import org.example.lc.pojo.response.ResponseBean;
import org.example.lc.service.ResourceService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LdapServiceImpl implements ResourceService {
    @Override
    public ResponseBean<Boolean> apply(ApplyInfo info) {
        log.info("Ldap开始申请资源");
        return null;
    }

    @Override
    public ResponseBean<Boolean> recovery(RecoveryInfo info) {
        log.info("Ldap开始回收资源");
        return null;
    }

    @Override
    public ResponseBean<Boolean> cancel(QuitInfo info) {
        log.info("Ldap开始离职流程");
        return null;
    }



    @Override
    public String resourceCode() {
        return ResourceEnum.ldap.getCode();
    }
}
