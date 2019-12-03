package com.github.manager.service;

import com.github.common.page.Page;
import com.github.common.page.PageInfo;
import com.github.manager.model.ManagerMenu;
import com.github.manager.model.ManagerPermission;
import com.github.manager.model.ManagerRole;
import com.github.manager.model.ManagerUser;

import java.util.List;

public interface ManagerService {

    /** 验证登录并返回用户信息 */
    ManagerUser login(String userName, String password);
    ManagerUser getUser(Long userId);
    /** 管理员查询用户 */
    PageInfo<ManagerUser> queryUser(String userName, Boolean status, Page page);
    /** 管理员添加或修改用户(包括重置密码, 修改状态) */
    void addOrUpdateUser(ManagerUser user);
    /** 管理员删除用户 */
    void deleteUser(Long userId);
    /** 用户修改自己的密码 */
    void updatePassword(Long userId, String oldPass, String newPass);

    /** 用户在查询自己的角色, 返回的每个角色对象包含菜单(无层级关系)和权限 */
    List<ManagerRole> getUserRole(ManagerUser user);
    List<ManagerRole> queryBasicRole();
    /** 管理员查询角色. 返回的每个角色对象里面菜单(带层级关系), 每个菜单下有权限 */
    List<ManagerRole> queryRole(String name);
    void addOrUpdateRole(ManagerRole role);
    void deleteRole(Long roleId);

    List<ManagerMenu> queryMenu(String name);
    void addOrUpdateMenu(ManagerMenu menu);
    void deleteMenu(Long menuId);
    void deleteMenus(List<Long> mids);

    List<ManagerPermission> queryPermission(String name);
    void addOrUpdatePermission(ManagerPermission permission);
    void deletePermission(Long permissionId);
    void deletePermissions(List<Long> pids);
}
