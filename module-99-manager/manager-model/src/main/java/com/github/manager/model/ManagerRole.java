package com.github.manager.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/** 角色, 与 用户是 多对多 的关系 --> t_manager_role */
@Data
public class ManagerRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 角色名 --> name */
    private String name;


    // 下面的字段不与数据库关联, 只做为数据载体进行传输

    /** 角色下的菜单 id */
    private List<Long> mids;
    /** 角色下的菜单 */
    private List<ManagerMenu> menus;

    /** 角色下的权限 id */
    private List<Long> pids;
    /** 角色下的权限 */
    private List<ManagerPermission> permissions;
}
