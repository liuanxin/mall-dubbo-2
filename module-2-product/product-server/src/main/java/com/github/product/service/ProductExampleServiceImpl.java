package com.github.product.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.common.Const;
import com.github.common.json.JsonUtil;
import com.github.common.page.Page;
import com.github.common.page.PageInfo;
import com.github.common.page.Pages;
import com.github.common.util.LogUtil;
import com.github.product.model.DemoModel;

@Service(version = Const.DUBBO_VERSION, timeout = Const.DUBBO_TIMEOUT, filter = Const.DUBBO_FILTER, interfaceClass = ProductExampleService.class)
public class ProductExampleServiceImpl implements ProductExampleService {

    /*
    private final ProductMapper productMapper;

    public ProductExampleServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }
    */

    @Override
    public PageInfo<DemoModel> demo(Page page) {
        if (LogUtil.ROOT_LOG.isDebugEnabled()) {
            LogUtil.ROOT_LOG.debug("test param: " + JsonUtil.toJson(page));
        }
        // return Pages.returnPage(productMapper.selectByExample(xx, Pages.param(page)));
        return Pages.returnPage(null);
    }
}
