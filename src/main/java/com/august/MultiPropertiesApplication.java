package com.august;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class MultiPropertiesApplication {
    //定义日志记录器，用于完成日志信息的记录
    static final Logger LOGGER = LoggerFactory.getLogger(MultiPropertiesApplication.class);

    public static void main(String[] args) {
        Environment env = SpringApplication.run(MultiPropertiesApplication.class, args).getEnvironment();
        try {
            LOGGER.info("\n----------------------------------------------------------\n\t" +
                            "MultiPropertiesApplication '{}' is running! Access URLs:\n\t" +
                            "Local: \t\thttp://{}:{}{} \n\t" +
                            "External: \thttp://{}:{}{} \n\t" +
                            "Profile(s): \t{}\n----------------------------------------------------------",
                    env.getProperty("spring.application.name") + "(" + env.getProperty("system.version") + ")",
                    InetAddress.getLoopbackAddress().getHostAddress(),
                    env.getProperty("server.port"),
                    env.getProperty("server.contextPath"),
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port"),
                    env.getProperty("server.contextPath"),
                    env.getProperty("spring.profiles.active"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @PostConstruct
    public void initBaseInfo() {
        if (IPUtils.LOCAL_HOST_INFO == null || IPUtils.LOCAL_HOST_INFO.size() < 1) {
            IPUtils.getLocalHost();
        }
        if (IPUtils.LOOPBACK_HOST_INFO == null || IPUtils.LOOPBACK_HOST_INFO.size() < 1) {
            IPUtils.getLoopbackHost();
        }
        if (IPUtils.IP_LIST == null || IPUtils.IP_LIST.size() < 1) {
            IPUtils.getIpList();
        }
    }
}
