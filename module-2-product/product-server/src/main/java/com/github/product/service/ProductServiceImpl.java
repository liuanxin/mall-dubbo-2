package com.github.product.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.common.Const;

@Service(version = Const.DUBBO_VERSION, timeout = Const.DUBBO_TIMEOUT, filter = Const.DUBBO_FILTER, interfaceClass = ProductService.class)
public class ProductServiceImpl implements ProductService {

    @Override
    public String example(String name) {
        return "hello " + name;
    }
}
