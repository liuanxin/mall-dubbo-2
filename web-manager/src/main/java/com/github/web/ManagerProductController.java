package com.github.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.common.Const;
import com.github.common.json.JsonResult;
import com.github.common.page.Page;
import com.github.common.page.PageInfo;
import com.github.liuanxin.api.annotation.ApiGroup;
import com.github.product.constant.ProductConst;
import com.github.product.model.DemoModel;
import com.github.product.service.ProductExampleService;
import com.github.vo.DemoVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.common.json.JsonResult.success;

@ApiGroup({ ProductConst.MODULE_INFO })
@RestController
public class ManagerProductController {

    @Reference(version = Const.DUBBO_VERSION, lazy = true, check = false, timeout = Const.DUBBO_TIMEOUT)
    private ProductExampleService productExampleService;

    @GetMapping("/demo")
    public JsonResult<PageInfo<DemoVo>> demo(Page page) {
        PageInfo<DemoModel> productPage = productExampleService.demo(page);
        return success("demo", DemoVo.assemblyData(productPage));
    }
}
