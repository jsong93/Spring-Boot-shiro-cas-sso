@[toc](Spring Boot shiro+cas单点登录)
项目架构
2.1.11.RELEASE
根据SpringMVC项目改写
[SpringMVC shiro+cas 单点登录](https://blog.csdn.net/JsongNeu/article/details/104301042)
## 搭建Cas服务器
[cas服务器搭建 md5+盐加密](https://blog.csdn.net/JsongNeu/article/details/104227262)
## 客户端
### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.jsong.wiki</groupId>
    <artifactId>SpringBoot-shiro-cas</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.11.RELEASE</version>
    </parent>
    <properties>
        <shiro.version>1.2.3</shiro.version>
        <lombok.version>1.16.14</lombok.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--spring ConfigurationProperties-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <!--lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>


        <!--        shiro start-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>${shiro.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-cas</artifactId>
            <version>${shiro.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>${shiro.version}</version>
        </dependency>

        <!--        shiro end-->

        <!--test-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-devtools</artifactId>
                <version>2.1.11.RELEASE</version>
                <configuration>
                    <fork>true</fork>
                </configuration>
            </plugin>
        </plugins>

        <!--        <resources>-->
        <!--            <resource>-->
        <!--                <directory>-->
        <!--                    src/main/webapp-->
        <!--                </directory>-->
        <!--                <targetPath>META-INF/resources</targetPath>-->
        <!--                <includes>-->
        <!--                    <include>**/**</include>-->
        <!--                </includes>-->
        <!--            </resource>-->
        <!--        </resources>-->
    </build>
</project>
```
### 继承CasRealm
CasRealm里定义了cas身份认证和权限鉴定的方法，继承CasRealm类，重写我们自己的认证 和 鉴权方法

- doGetAuthenticationInfo方法
>当用户登录认证时进入改方法

- doGetAuthorizationInfo方法
>当访问需要授权的资源时，进入该方法

```java
package com.jsong.wiki.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShiroCasRealm extends CasRealm {
    /**
     * 设置角色 权限信息
     * @param principals
     * @return
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String account = (String) principals.getPrimaryPrincipal();
        System.out.println(account);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /**
     * cas 认证
     * @param token
     * @return
     */
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token){
        AuthenticationInfo authc = super.doGetAuthenticationInfo(token);
        return authc;
    }
}

```

### application.yml
```yml
server:
  port: 18081

# shiro 路径配置
shiro:
  loginUrl: https://127.0.0.1:8080/cas/login?service=http://127.0.0.1:18081/shiro-cas
  logoutUrl: https://127.0.0.1:8080/cas/logout?service=http://127.0.0.1:18081/shiro-cas
  successUrl: /
  failureUrl: /login
  # 过滤器配置
  casFilterUrlPattern: /shiro-cas
  unauthorizedUrl: /unauthorizedUrl.html
  logoutUrlPattern: /logout
#  "[]" 转义字符
  authUrlPatternMap: {"[/**]": authc}
#    - {"/**":authc}
  cas:
    serverUrlPrefix: https://127.0.0.1:8080/cas
    service: http://127.0.0.1:18081/shiro-cas

#thymeleaf配置
spring:
  thymeleaf:
    prefix: /templates/**
    suffix: .html
  resources:
    static-locations: classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/, classpath:/templates/

```
### UrlBean
读取shiro配置文件
ShiroUrl.java
```java
package com.jsong.wiki.shiro.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

```
CasUrl.java
```java
package com.jsong.wiki.shiro.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class CasUrl {
    private  String serverUrlPrefix;
    private  String service;
}

```

### ShiroConfig
对应SpringMVC中 shiro-context.xml
```java
package com.jsong.wiki.shiro.config;

import com.jsong.wiki.shiro.bean.ShiroUrl;
import com.jsong.wiki.shiro.realm.ShiroCasRealm;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    // shiroFilter
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager,
                                              @Qualifier("casFilter") CasFilter casFilter,
//                                              @Qualifier("logoutFilter") LogoutFilter logoutFilter,
                                              ShiroUrl shiroUrl) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        设置登录URL,当用户未认证,但访问了需要认证后才能访问的页面,就会自动重定向到登录URL
        shiroFilterFactoryBean.setLoginUrl(shiroUrl.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroUrl.getSuccessUrl());
//        设置没有权限的URL,当用户认证后,访问一个页面却没有权限时,就会自动重定向到没有权限的URL,若用户未认证访问一个需要权限的URL时,会跳转到登录URL
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroUrl.getUnauthorizedUrl());

        Map<String, Filter> filters = new HashMap<>();
        filters.put("casFilter", casFilter);
//        filters.put("logoutFilter", logoutFilter);
//        将Filter添加到Shiro过滤器链中,用于对资源设置权限
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
        filterChainDefinitionMap.put(shiroUrl.getCasFilterUrlPattern(), "casFilter");
//        filterChainDefinitionMap.put(shiroUrl.getLogoutUrlPattern(), "logoutFilter");
        filterChainDefinitionMap.putAll(shiroUrl.getAuthUrlPatternMap());
        // 配置哪些请求需要受保护,以及访问这些页面需要的权限
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    // 认证filter
    @Bean
    public CasFilter casFilter(ShiroUrl shiroUrl) {
        CasFilter casFilter = new CasFilter();
        // 登录成功url
        casFilter.setSuccessUrl(shiroUrl.getSuccessUrl());
        // 登录失败url
        casFilter.setFailureUrl(shiroUrl.getFailureUrl());
        return casFilter;
    }

    // 自定义 casRealm
    @Bean
    public ShiroCasRealm casRealm(ShiroUrl shiroUrl) {
        ShiroCasRealm casRealm = new ShiroCasRealm();
        // cas服务器
        casRealm.setCasServerUrlPrefix(shiroUrl.getCas().getServerUrlPrefix());
        // 客户端地址，用于接收tiket
        casRealm.setCasService(shiroUrl.getCas().getService());
        return casRealm;
    }

    /**
     * 问题？？？？？
     * 把这个bean注入后，登录后就会重定向到登出，为什么呢？？？？？
     * @param shiroUrl
     * @return
     */
//    @Bean
    public LogoutFilter logoutFilter(ShiroUrl shiroUrl) {
        LogoutFilter logoutFilter = new LogoutFilter();
        // 登出后重定向地址
        logoutFilter.setRedirectUrl(shiroUrl.getLogoutUrl());
        return logoutFilter;
    }

    //    配置securityManager
    @Bean
    public DefaultWebSecurityManager securityManager(ShiroCasRealm casRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSubjectFactory(new CasSubjectFactory());
        securityManager.setRealm(casRealm);
        return securityManager;
    }

    //    配置lifecycleBeanPostProcessor,shiro bean的生命周期管理器,可以自动调用Spring IOC容器中shiro bean的生命周期方法(初始化/销毁)
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /*    为了支持Shiro的注解需要定义DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor两个bean
        配置DefaultAdvisorAutoProxyCreator,必须配置了lifecycleBeanPostProcessor才能使用*/
//    @DependsOn("lifecycleBeanPostProcessor")
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        return defaultAdvisorAutoProxyCreator;
    }

    //    配置AuthorizationAttributeSourceAdvisor
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}

```

---
## 遗留问题？？？
在ShiroConfig中配置LogoutFilter的后，登录后就会重定向到登出页面，求大神解答
