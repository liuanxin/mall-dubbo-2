package com.github.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 用户 和 角色 的中间表 --> t_manager_user_role */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户 id --> uid */
    private Long uid;

    /** 角色 id --> rid */
    private Long rid;
}
