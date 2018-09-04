package com.github.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.common.Const;
import com.github.common.json.JsonResult;
import com.github.common.service.CommonService;
import com.github.common.util.U;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @Reference(version = Const.DUBBO_VERSION, lazy = true, check = false, timeout = Const.DUBBO_TIMEOUT)
    private CommonService commonService;

    @ResponseBody
    @GetMapping("/example")
    public JsonResult example(String name) {
        if (U.isBlank(name)) {
            name = U.EMPTY;
        }
        return JsonResult.success(commonService.example(name));
    }
}
