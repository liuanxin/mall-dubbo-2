package com.github.manager.service;

import com.github.common.encrypt.Encrypt;
import com.github.common.page.Page;
import com.github.common.page.PageInfo;
import com.github.common.page.Pages;
import com.github.common.util.A;
import com.github.common.util.U;
import com.github.liuanxin.page.model.PageBounds;
import com.github.manager.model.*;
import com.github.manager.repository.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerUserMapper userMapper;
    private final ManagerRoleMapper roleMapper;
    private final ManagerMenuMapper menuMapper;
    private final ManagerPermissionMapper permissionMapper;

    private final ManagerUserRoleMapper userRoleMapper;
    private final ManagerRoleMenuMapper roleMenuMapper;
    private final ManagerRolePermissionMapper rolePermissionMapper;

    public ManagerServiceImpl(ManagerUserMapper userMapper,
                              ManagerRoleMapper roleMapper,
                              ManagerMenuMapper menuMapper,
                              ManagerPermissionMapper permissionMapper,

                              ManagerUserRoleMapper userRoleMapper,
                              ManagerRoleMenuMapper roleMenuMapper,
                              ManagerRolePermissionMapper rolePermissionMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.permissionMapper = permissionMapper;

        this.userRoleMapper = userRoleMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }


    @Override
    public ManagerUser login(String username, String password) {
        ManagerUserExample userExample = new ManagerUserExample();
        userExample.or().andUsernameEqualTo(username);
        ManagerUser user = A.first(userMapper.selectByExample(userExample, new PageBounds(1)));

        U.assertNil(user, "无此用户");
        return user;
    }
    @Override
    public List<ManagerRole> getUserRole(ManagerUser user) {
        // 非管理员才填充数据, 管理员不需要
        if (user.notAdmin()) {
            ManagerUserRoleExample userRoleExample = new ManagerUserRoleExample();
            userRoleExample.or().andUidEqualTo(user.getId());
            List<ManagerUserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);

            List<Long> rids = Lists.transform(userRoles, ManagerUserRole::getRid);
            // 将用户所有角色下的菜单和权限赋值到用户底下
            return fillRole(rids, false);
        } else {
            return Collections.emptyList();
        }
    }
    /**
     * 填充角色的菜单和权限<br><br>
     *
     * 管理员在角色管理中查询角色信息时权限赋值在菜单里, 菜单(有层级关系)赋值在角色里<br>
     * 用户在查询自己的角色信息时权限和菜单(无层级关系)赋值在角色里
     *
     * @param rids 角色 id
     * @param hasAdminManager 是否是管理员在操作角色管理
     */
    private List<ManagerRole> fillRole(List<Long> rids, boolean hasAdminManager) {
        List<ManagerRole> roles = null;
        List<ManagerMenu> menus = null;
        // RoleId 为 key, List<MenuId> 为 value 的 Map
        Map<String, Collection<Long>> roleIdMenuIdMap = null;
        List<ManagerPermission> permissions = null;
        if (A.isNotEmpty(rids)) {
            ManagerRoleExample roleExample = new ManagerRoleExample();
            roleExample.or().andIdIn(rids);
            roles = roleMapper.selectByExample(roleExample);

            ManagerRoleMenuExample roleMenuExample = new ManagerRoleMenuExample();
            roleMenuExample.or().andRidIn(rids);
            List<ManagerRoleMenu> roleMenus = roleMenuMapper.selectByExample(roleMenuExample);
            if (A.isNotEmpty(roleMenus)) {
                List<Long> mids = Lists.transform(roleMenus, ManagerRoleMenu::getMid);
                if (A.isNotEmpty(mids)) {
                    ManagerMenuExample menuExample = new ManagerMenuExample();
                    menuExample.or().andIdIn(mids);
                    menus = menuMapper.selectByExample(menuExample);
                }

                Multimap<String, Long> roleMenuMultimap = ArrayListMultimap.create();
                for (ManagerRoleMenu roleMenu : roleMenus) {
                    roleMenuMultimap.put(U.toStr(roleMenu.getRid()), roleMenu.getMid());
                }
                roleIdMenuIdMap = roleMenuMultimap.asMap();
            }

            ManagerRolePermissionExample rolePermissionExample = new ManagerRolePermissionExample();
            rolePermissionExample.or().andRidIn(rids);
            List<ManagerRolePermission> rolePermissions = rolePermissionMapper.selectByExample(rolePermissionExample);
            if (A.isNotEmpty(rolePermissions)) {
                List<Long> pids = Lists.transform(rolePermissions, ManagerRolePermission::getPid);
                if (A.isNotEmpty(pids)) {
                    ManagerPermissionExample permissionExample = new ManagerPermissionExample();
                    permissionExample.or().andIdIn(pids);
                    permissions = permissionMapper.selectByExample(permissionExample);
                }
            }
        }
        // 管理员在角色管理中查询角色信息时权限赋值在菜单里, 菜单(有层级关系)赋值在角色里
        // 用户在查询自己的角色信息时权限和菜单(无层级关系)赋值在角色里
        if (hasAdminManager) {
            if (A.isNotEmpty(roles)) {
                Map<String, List<ManagerMenu>> roleMenuMap = ManagerMenu.handleRelation(menus, permissions, roleIdMenuIdMap);
                for (ManagerRole role : roles) {
                    role.setMenus(roleMenuMap.get(U.toStr(role.getId())));
                }
            }
        } else {
            if (A.isNotEmpty(roles)) {
                for (ManagerRole role : roles) {
                    role.setMenus(menus);
                    role.setPermissions(permissions);
                }
            }
        }
        return roles;
    }
    @Override
    public ManagerUser getUser(Long userId) {
        U.assert0(userId, "没有这个用户");
        ManagerUser user = userMapper.selectByPrimaryKey(userId);
        U.assertNil(user, "无此用户");

        user.assignmentData(getUserRole(user));
        return user;
    }
    @Override
    public PageInfo<ManagerUser> queryUser(String username, Boolean status, Page page) {
        ManagerUserExample userExample = new ManagerUserExample();
        ManagerUserExample.Criteria or = userExample.or();
        if (U.isNotBlank(username)) {
            or.andUsernameLike(U.like(username));
        }
        if (U.isNotBlank(status)) {
            or.andStatusEqualTo(status);
        }
        return Pages.returnPage(userMapper.selectByExample(userExample, Pages.param(page)));
    }
    @Override
    public void addOrUpdateUser(ManagerUser user) {
        Long uid = user.getId();
        if (U.greater0(uid)) {
            ManagerUser u = userMapper.selectByPrimaryKey(uid);
            U.assertNil(u, "没有这个用户, 无法修改");

            if (U.isNotBlank(u.getPassword())) {
                U.assertException(u.getHasAdmin(), "不能重置管理员密码, 请使用旧密码进行修改");
                U.assertException(U.isNotBlank(u.getStatus()) && u.getStatus(), "用户已被禁用, 请先解禁再修改密码");

                user.setPassword(Encrypt.bcryptEncode(user.getPassword()));
            }
            userMapper.updateByPrimaryKeySelective(user);
        } else {
            ManagerUserExample userExample = new ManagerUserExample();
            userExample.or().andUsernameEqualTo(user.getUsername());
            int count = userMapper.countByExample(userExample);
            U.assertException(count > 0, "已经有同名用户, 不能再次添加");

            user.setId(null);
            user.setPassword(Encrypt.bcryptEncode(user.getPassword()));
            userMapper.insertSelective(user);
            uid = user.getId();
        }

        List<Long> rids = user.getRids();
        if (A.isNotEmpty(rids)) {
            List<ManagerUserRole> userRoles = Lists.newArrayList();
            for (Long rid : rids) {
                if (U.greater0(rid)) {
                    userRoles.add(new ManagerUserRole(uid, rid));
                }
            }
            if (A.isNotEmpty(userRoles)) {
                ManagerUserRoleExample userRoleExample = new ManagerUserRoleExample();
                userRoleExample.or().andUidEqualTo(uid);
                userRoleMapper.deleteByExample(userRoleExample);
                userRoleMapper.batchInsert(userRoles);
            }
        }
    }
    @Override
    public void updatePassword(Long userId, String oldPass, String newPass) {
        U.assert0(userId, "无此用户");

        ManagerUser user = userMapper.selectByPrimaryKey(userId);
        U.assertNil(user, "没有这个用户");
        U.assertException(U.isNotBlank(user.getStatus()) && user.getStatus(), "用户不能登录");
        U.assertException(Encrypt.checkNotBcrypt(oldPass, user.getPassword()), "旧密码有误");

        ManagerUser update = new ManagerUser();
        update.setId(userId);
        update.setPassword(Encrypt.bcryptEncode(newPass));
        userMapper.updateByPrimaryKeySelective(update);
    }
    @Override
    public void deleteUser(Long id) {
        U.assert0(id, "无此用户");
        int flag = userMapper.deleteByPrimaryKey(id);
        if (flag == 1) {
            ManagerUserRoleExample userRoleExample = new ManagerUserRoleExample();
            userRoleExample.or().andUidEqualTo(id);
            userRoleMapper.deleteByExample(userRoleExample);
        }
    }


    @Override
    public List<ManagerRole> queryBasicRole() {
        return roleMapper.selectByExample(null);
    }
    @Override
    public List<ManagerRole> queryRole(Long userId) {
        List<Long> rids;
        if (U.greater0(userId)) {
            ManagerUserRoleExample userRoleExample = new ManagerUserRoleExample();
            userRoleExample.or().andUidEqualTo(userId);
            List<ManagerUserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);
            rids = Lists.transform(userRoles, ManagerUserRole::getRid);
        } else {
            List<ManagerRole> roles = roleMapper.selectByExample(null);
            rids = Lists.transform(roles, ManagerRole::getId);
        }
        return fillRole(rids, true);
    }
    @Override
    @Transactional
    public void addOrUpdateRole(ManagerRole role) {
        Long rid = role.getId();
        if (U.greater0(rid)) {
            ManagerRoleExample roleExample = new ManagerRoleExample();
            roleExample.or().andIdEqualTo(rid);
            int count = roleMapper.countByExample(roleExample);
            U.assertException(count == 0, "没有这个角色, 无法修改");

            roleMapper.updateByPrimaryKeySelective(role);
        } else {
            ManagerRoleExample roleExample = new ManagerRoleExample();
            roleExample.or().andNameEqualTo(role.getName());
            int count = roleMapper.countByExample(roleExample);
            U.assertException(count > 0, "已经有同名角色, 不能再次添加");

            role.setId(null);
            roleMapper.insertSelective(role);
            rid = role.getId();
        }

        List<Long> mids = role.getMids();
        if (A.isNotEmpty(mids)) {
            List<ManagerRoleMenu> roleMenus = Lists.newArrayList();
            for (Long mid : mids) {
                if (U.greater0(mid)) {
                    roleMenus.add(new ManagerRoleMenu(rid, mid));
                }
            }
            if (A.isNotEmpty(roleMenus)) {
                ManagerRoleMenuExample roleMenuExample = new ManagerRoleMenuExample();
                roleMenuExample.or().andRidEqualTo(rid);
                roleMenuMapper.deleteByExample(roleMenuExample);
                roleMenuMapper.batchInsert(roleMenus);
            }
        }

        List<Long> pids = role.getPids();
        if (A.isNotEmpty(pids)) {
            List<ManagerRolePermission> rolePermissions = Lists.newArrayList();
            for (Long pid : pids) {
                if (U.greater0(pid)) {
                    rolePermissions.add(new ManagerRolePermission(rid, pid));
                }
            }
            if (A.isNotEmpty(rolePermissions)) {
                ManagerRolePermissionExample rolePermissionExample = new ManagerRolePermissionExample();
                rolePermissionExample.or().andRidEqualTo(rid);
                rolePermissionMapper.deleteByExample(rolePermissionExample);
                rolePermissionMapper.batchInsert(rolePermissions);
            }
        }
    }
    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        U.assert0(roleId, "无此角色");

        ManagerUserRoleExample userRoleExample = new ManagerUserRoleExample();
        userRoleExample.or().andRidEqualTo(roleId);
        int count = userRoleMapper.countByExample(userRoleExample);
        U.assertException(count > 0, "有用户分配了此角色, 请先取消分配再删除");

        int flag = roleMapper.deleteByPrimaryKey(roleId);
        if (flag == 1) {
            ManagerRoleMenuExample roleMenuExample = new ManagerRoleMenuExample();
            roleMenuExample.or().andRidEqualTo(roleId);
            roleMenuMapper.deleteByExample(roleMenuExample);

            ManagerRolePermissionExample rolePermissionExample = new ManagerRolePermissionExample();
            rolePermissionExample.or().andRidEqualTo(roleId);
            rolePermissionMapper.deleteByExample(rolePermissionExample);
        }
    }


    @Override
    public List<ManagerMenu> queryMenu(String name) {
        ManagerMenuExample menuExample = new ManagerMenuExample();
        if (U.isNotBlank(name)) {
            menuExample.or().andNameLike(U.like(name));
        }
        return menuMapper.selectByExample(menuExample);
    }
    @Override
    public void addOrUpdateMenu(ManagerMenu menu) {
        Long mid = menu.getId();
        if (U.greater0(mid)) {
            ManagerMenu m = menuMapper.selectByPrimaryKey(mid);
            U.assertNil(m, "没有这个菜单, 无法修改");

            menuMapper.updateByPrimaryKeySelective(menu);
        } else {
            ManagerMenuExample menuExample = new ManagerMenuExample();
            menuExample.or().andNameEqualTo(menu.getName());
            int count = menuMapper.countByExample(menuExample);
            U.assertException(count > 0, "已经有同名菜单, 不能再次添加");

            menu.setId(null);
            menuMapper.insertSelective(menu);
        }
    }
    @Override
    public void deleteMenu(Long menuId) {
        U.assert0(menuId, "无此菜单");

        ManagerPermissionExample permissionExample = new ManagerPermissionExample();
        permissionExample.or().andMidEqualTo(menuId);
        int count = permissionMapper.countByExample(permissionExample);
        U.assertException(count > 0, "此菜单下已经有权限了, 请先将权限删除再来删除菜单");

        menuMapper.deleteByPrimaryKey(menuId);
    }
    @Override
    public void deleteMenus(List<Long> mids) {
        if (A.isNotEmpty(mids)) {
            ManagerPermissionExample permissionExample = new ManagerPermissionExample();
            permissionExample.or().andMidIn(mids);
            int count = permissionMapper.countByExample(permissionExample);
            U.assertException(count > 0, "传入的菜单下已经有权限了, 请先将权限删除再来删除菜单");

            ManagerMenuExample menuExample = new ManagerMenuExample();
            menuExample.or().andIdIn(mids);
            menuMapper.deleteByExample(menuExample);
        }
    }


    @Override
    public List<ManagerPermission> queryPermission(String name) {
        ManagerPermissionExample permissionExample = new ManagerPermissionExample();
        if (U.isNotBlank(name)) {
            permissionExample.or().andNameLike(U.like(name));
        }
        return permissionMapper.selectByExample(permissionExample);
    }
    @Override
    public void addOrUpdatePermission(ManagerPermission permission) {
        Long pid = permission.getId();
        if (U.greater0(pid)) {
            ManagerPermission p = permissionMapper.selectByPrimaryKey(pid);
            U.assertNil(p, "没有这个权限, 无法修改");

            permissionMapper.updateByPrimaryKeySelective(permission);
        } else {
            ManagerPermissionExample permissionExample = new ManagerPermissionExample();
            permissionExample.or().andMethodEqualTo(permission.getMethod()).andUrlEqualTo(permission.getUrl());
            int count = permissionMapper.countByExample(permissionExample);
            U.assertException(count > 0, "已经有同样规则的权限, 不能再次添加");

            permission.setId(null);
            permissionMapper.insertSelective(permission);
        }
    }
    @Override
    public void deletePermission(Long permissionId) {
        permissionMapper.deleteByPrimaryKey(permissionId);
    }
    @Override
    public void deletePermissions(List<Long> pids) {
        if (A.isNotEmpty(pids)) {
            ManagerPermissionExample permissionExample = new ManagerPermissionExample();
            permissionExample.or().andIdIn(pids);
            permissionMapper.deleteByExample(permissionExample);
        }
    }
}
