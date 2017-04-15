package com.august;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * PROJECT_NAME: multiProperties
 * PACKAGE_NAME: com.august
 * Author: August
 * Update: August(2017/4/13)
 * Description:用于测试文件变动的信息
 */
@RestController
public class IndexController {
    //加载配置文件中的参数

    @Value(value = "${system.version}")
    private String version;
    @Value(value = "${server.contextPath}")
    private String contextPath;
    @Value(value = "${spring.application.name}")
    private String applicationName;
    @Value(value = "${spring.profiles.active}")
    private String profileActive;


    @RequestMapping(value = {"/", "/index"})
    public Map<String, Object> index() {

        Map<String, Object> map = new HashMap<>();
        map.put("ApplicationName(应用名称)", applicationName);
        map.put("Version(版本信息)", version);
        map.put("ContextPath(请求根目录)", contextPath);
        map.put("ProfileActive(当前环境)", profileActive);
        map.put("localHostInfo(本地主机信息)", IPUtils.LOCAL_HOST_INFO);
        map.put("LoopbackHostInfo(本地主机信息)", IPUtils.LOOPBACK_HOST_INFO);
        map.put("IpList(本地主机IP列表信息)", IPUtils.IP_LIST);
        return map;
    }


}