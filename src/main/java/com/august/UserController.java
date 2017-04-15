package com.august;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
@RequestMapping("/user")
public class UserController {

    //加载配置文件中的参数

    @Value(value = "${august.name}")
    private String name;
    @Value(value = "${august.age}")
    private int age;
    @Value(value = "${august.desc}")
    private String desc;


    @RequestMapping
    public Map<String, Object> index() {

        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Age", age);
        map.put("Desc", desc);
        map.put("Date", new Date());
        return map;
    }
}