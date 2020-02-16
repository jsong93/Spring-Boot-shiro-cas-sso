package com.jsong.wiki.shiro;

import com.jsong.wiki.shiro.bean.ShiroUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShiroCasTestApplication {
    @Autowired
    private ShiroUrl shiroUrl;

    @Test
    public void readShiroUrl(){
        System.out.println(shiroUrl.getLoginUrl());
        System.out.println(shiroUrl.getCas().getServerUrlPrefix());
    }
}
