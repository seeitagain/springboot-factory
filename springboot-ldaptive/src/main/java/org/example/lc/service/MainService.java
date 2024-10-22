package org.example.lc.service;

import lombok.extern.slf4j.Slf4j;
import org.example.lc.pojo.domain.ApplyInfo;
import org.example.lc.pojo.domain.QuitInfo;
import org.example.lc.pojo.domain.RecoveryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MainService {

    private final Map<String, ResourceService> serviceMap;

    @Autowired
    public MainService(List<ResourceService> services) {
        this.serviceMap = services.stream()
                .collect(Collectors.toMap(ResourceService::resourceCode, service -> service));
    }

    /**
     * 申请资源
     *
     * @param resourceCode
     * @param info
     */
    public void apply(String resourceCode, ApplyInfo info) {
        ResourceService service = serviceMap.get(resourceCode);
        if (service != null) {
            service.apply(info);
        } else {
            log.info("No service found for resourceCode:{}", resourceCode);
        }
    }

    /**
     * 回收资源
     *
     * @param resourceCode
     * @param info
     */
    public void recovery(String resourceCode, RecoveryInfo info) {
        ResourceService service = serviceMap.get(resourceCode);
        if (service != null) {
            service.recovery(info);
        } else {
            log.info("No service found for resourceCode:{}", resourceCode);
        }
    }

    /**
     * 员工离职
     * @param resourceCode
     * @param info
     */
    public void quit(String resourceCode, QuitInfo info) {
        ResourceService service = serviceMap.get(resourceCode);
        if (service != null) {
            service.cancel(info);
        } else {
            log.info("No service found for resourceCode:{}", resourceCode);
        }
    }
}