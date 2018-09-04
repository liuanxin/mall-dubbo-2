package com.github.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.common.Const;

@Service(version = Const.DUBBO_VERSION, timeout = Const.DUBBO_TIMEOUT, filter = Const.DUBBO_FILTER, interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Override
    public String example(String name) {
        return "hello " + name;
    }
}
