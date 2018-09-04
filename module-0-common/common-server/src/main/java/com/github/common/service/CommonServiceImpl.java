package com.github.common.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.common.Const;

@Service(version = Const.DUBBO_VERSION, timeout = Const.DUBBO_TIMEOUT, filter = Const.DUBBO_FILTER, interfaceClass = CommonService.class)
public class CommonServiceImpl implements CommonService {

    @Override
    public String example(String name) {
        return "hello " + name;
    }
}
