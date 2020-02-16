package com.jsong.wiki.shiro.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroUrl {
    private String loginUrl;
    private String logoutUrl;
    private String successUrl;
    private String failureUrl;
    private String unauthorizedUrl;
    private String casFilterUrlPattern;
    private String logoutUrlPattern;
    private Map<String, String> authUrlPatternMap;

    private CasUrl cas;
}
