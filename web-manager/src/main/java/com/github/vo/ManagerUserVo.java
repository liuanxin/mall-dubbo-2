package com.github.vo;

import com.github.common.json.JsonUtil;
import com.github.common.util.A;
import com.github.common.util.U;
import com.github.liuanxin.api.annotation.ApiReturn;
import com.github.manager.model.ManagerMenu;
import com.github.manager.model.ManagerUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ManagerUserVo {

    @ApiReturn("用户id")
    private Long id;

    @ApiReturn("用户名")
    private String userName;

    @ApiReturn("昵称")
    private String nickName;

    @ApiReturn("头像")
    private String avatar;

    @ApiReturn("true 则表示是管理员")
    private boolean hasAdmin;

    @ApiReturn("用户能见到的菜单")
    private List<ManagerMenuVo> menus;

    @Data
    @Accessors(chain = true)
    public static class ManagerMenuVo {
        @ApiReturn("菜单说明")
        private String name;

        @ApiReturn("前端对应的值")
        private String front;

        @ApiReturn("子菜单")
        private List<ManagerMenuVo> children;
    }


    public static ManagerUserVo assemblyData(ManagerUser user, List<ManagerMenu> menus) {
        ManagerUserVo vo = JsonUtil.convert(user, ManagerUserVo.class);
        if (U.isNotBlank(vo)) {
            List<ManagerMenuVo> menuVos = JsonUtil.convertList(menus, ManagerMenuVo.class);
            if (A.isNotEmpty(menuVos)) {
                vo.setMenus(menuVos);
            }
        }
        return vo;
    }
}
