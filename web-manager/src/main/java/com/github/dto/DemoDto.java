package com.github.dto;

import com.github.liuanxin.api.annotation.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class DemoDto {

    @ApiParam("用户 id")
    private Long userId;
}
