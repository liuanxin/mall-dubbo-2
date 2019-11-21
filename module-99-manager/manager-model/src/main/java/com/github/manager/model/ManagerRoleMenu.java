package com.github.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 角色 和 菜单 的中间表 --> t_manager_role_menu */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 角色 id --> rid */
    private Long rid;

    /** 菜单 id --> mid */
    private Long mid;
}
