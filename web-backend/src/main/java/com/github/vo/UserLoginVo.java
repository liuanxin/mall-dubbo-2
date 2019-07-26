package com.github.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.common.Const;
import com.github.common.json.JsonUtil;
import com.github.common.util.U;
import com.github.liuanxin.api.annotation.ApiReturn;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginVo {

    private Long id;

    @ApiReturn("用户名")
    private String name;

    @ApiReturn("将此值保存到本地, 以后每次请求都带过来: " + Const.TOKEN + "=当前值, header 或 param 都可以")
    @JsonProperty(Const.TOKEN)
    private String token;

    /** 将 service 查询之后的数据组装成前端需要的 */
    public static <T> UserLoginVo assemblyData(T user, String token) {
        UserLoginVo returnVo = JsonUtil.convert(user, UserLoginVo.class);
        if (U.isNotBlank(returnVo)) {
            returnVo.setToken(token);
        }
        return returnVo;
    }
}
