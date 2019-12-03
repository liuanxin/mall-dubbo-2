package com.github.manager.model;

import com.github.common.util.A;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/** 用户 --> t_manager_user */
@Data
public class ManagerUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final List<String> ROOT_LIST = Arrays.asList("root", "admin");

    private Long id;

    /** 用户名 --> user_name */
    private String userName;

    /** 密码 --> password */
    private String password;

    /** 昵称 --> nick_name */
    private String nickName;

    /** 头像 --> avatar */
    private String avatar;

    /** 1 表示已禁用 --> status */
    private Boolean status;


    // 下面的字段不与数据库关联, 只做为数据载体进行传输

    /** 用户的角色 id, 添加修改时用到 */
    private List<Long> rids;

    /** 用户的所有角色下的所有菜单 */
    private List<ManagerMenu> menus;
    /** 用户的所有角色下的所有权限 */
    private List<ManagerPermission> permissions;

    /** 当前用户是管理员账号就返回 true */
    public boolean getHasAdmin() {
        return ROOT_LIST.contains(userName);
    }
    public boolean notAdmin() {
        return !getHasAdmin();
    }

    /** 一个用户有多个角色, 一个角色又有多个菜单(且会有父子层级关系)和多个权限, 基于用户的角色将菜单和权限赋值 */
    public void assignmentData(List<ManagerRole> roles) {
        if (A.isNotEmpty(roles)) {
            List<ManagerMenu> menuList = Lists.newArrayList();
            for (ManagerRole role : roles) {
                menuList.addAll(role.getMenus());
            }
            this.menus = ManagerMenu.handleAllMenu(menuList);

            Set<ManagerPermission> set = Sets.newHashSet();
            for (ManagerRole role : roles) {
                set.addAll(role.getPermissions());
            }
            this.permissions = Lists.newArrayList(set);
        }
    }
}
