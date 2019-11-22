package com.github.manager.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/** 权限, 对应每一个后台请求, 跟菜单是 多对一 的关系, 跟角色是 多对多 的关系 --> t_manager_permission */
@Data
public class ManagerPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 所属菜单 --> mid */
    private Long mid;

    /** 权限说明, 如(查询用户) --> name */
    private String name;

    /** GET 或 POST 等, * 表示通配 --> method */
    private String method;

    /** 如 /user --> url */
    private String url;


    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        ManagerPermission that = (ManagerPermission) o;
        return method.equals(that.method) && url.equals(that.url);
    }
    // 以 method 和 url 为唯一性
    @Override
    public int hashCode() {
        return Objects.hash(method, url);
    }
}
