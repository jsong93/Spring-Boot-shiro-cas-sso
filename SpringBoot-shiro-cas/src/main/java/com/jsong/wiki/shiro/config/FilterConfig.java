package com.jsong.wiki.shiro.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean shiroFileter() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setName("shiroFilter");
//        filterRegistrationBean.setFilter(new DelegatingFilterProxy("shiroFilter"));
//        filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.setEnabled(true);
//        return filterRegistrationBean;
//    }


}
