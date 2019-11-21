package com.github.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 角色 和 权限 的 中间表 --> t_manager_role_permission */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRolePermission implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 角色 id --> rid */
    private Long rid;

    /** 权限 id --> pid */
    private Long pid;
}
