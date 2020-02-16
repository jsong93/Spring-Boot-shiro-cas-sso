package com.jsong.wiki.shiro.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class CasUrl {
    private  String serverUrlPrefix;
    private  String service;
}
