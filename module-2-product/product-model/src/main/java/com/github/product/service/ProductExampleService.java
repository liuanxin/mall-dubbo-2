package com.github.product.service;

import com.github.common.page.Page;
import com.github.common.page.PageInfo;
import com.github.product.model.DemoModel;

public interface ProductExampleService {

    PageInfo<DemoModel> demo(Page page);
}
