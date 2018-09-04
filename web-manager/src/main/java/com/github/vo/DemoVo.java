package com.github.vo;

import com.github.common.page.PageInfo;
import com.github.product.model.DemoModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class DemoVo {

    private Long id;
    private String name;

    public static PageInfo<DemoVo> assemblyData(PageInfo<DemoModel> page) {
        return PageInfo.convert(page);
    }
}
