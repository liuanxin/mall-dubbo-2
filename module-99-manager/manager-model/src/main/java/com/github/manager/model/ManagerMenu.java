package com.github.manager.model;

import com.github.common.util.A;
import com.github.common.util.U;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** 菜单, 需要跟前端对应, 前端每增加一个菜单就需要添加一条记录, 与角色是 多对多 的关系 --> t_manager_menu */
@Data
public class ManagerMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final long ROOT_ID = 0L;

    private Long id;

    /** 父菜单, 0 则表示是根菜单 --> pid */
    private Long pid;

    /** 菜单说明 --> name */
    private String name;

    /** 前端对应的值(如 path 或 name) --> front */
    private String front;


    // 下面的字段不与数据库关联, 只做为数据载体进行传输

    /** 子菜单 */
    private List<ManagerMenu> childMenus;

    /** 菜单下的权限 id */
    private List<Long> permissionIds;
    /** 菜单下的权限 */
    private List<ManagerPermission> permissionList;

    private static void handle(ManagerMenu menu, Map<String, Collection<ManagerMenu>> menuMap) {
        Collection<ManagerMenu> menus = menuMap.get(U.toStr(menu.getId()));
        if (A.isNotEmpty(menus)) {
            for (ManagerMenu m : menus) {
                handle(m, menuMap);
            }
            menu.setChildMenus(Lists.newArrayList(menus));
        }
    }
    /** 将多条菜单整理成有父子关系的菜单, 且每条菜单中填充它下面的权限, 以 RoleId 为 key, List<Menu> 为 value 的 Map 形式返回 */
    public static Map<String, List<ManagerMenu>> handleRelation(List<ManagerMenu> menus,
                                                                List<ManagerPermission> permissions,
                                                                Map<String, Collection<Long>> roleIdMenuIdMap) {
        if (A.isEmpty(roleIdMenuIdMap) || A.isNotEmpty(menus)) {
            return Collections.emptyMap();
        }

        Multimap<String, ManagerPermission> perMap = LinkedListMultimap.create();
        if (A.isNotEmpty(permissions)) {
            for (ManagerPermission permission : permissions) {
                perMap.put(U.toStr(permission.getMid()), permission);
            }
        }

        List<ManagerMenu> menuList = Lists.newArrayList();
        Multimap<String, ManagerMenu> childMap = LinkedListMultimap.create();
        for (ManagerMenu menu : menus) {
            // 将权限写进菜单
            Collection<ManagerPermission> mps = perMap.get(U.toStr(menu.getId()));
            if (A.isNotEmpty(mps)) {
                menu.setPermissionList(Lists.newArrayList(mps));
            }

            if (menu.getPid() == ROOT_ID) {
                menuList.add(menu);
            } else {
                childMap.put(U.toStr(menu.getPid()), menu);
            }
        }
        Map<String, Collection<ManagerMenu>> relationMap = childMap.asMap();
        for (ManagerMenu menu : menuList) {
            handle(menu, relationMap);
        }

        Map<String, ManagerMenu> tmpMap = Maps.newHashMap();
        for (ManagerMenu menu : menuList) {
            tmpMap.put(U.toStr(menu.getId()), menu);
        }

        Map<String, List<ManagerMenu>> returnMap = Maps.newHashMap();
        if (A.isNotEmpty(tmpMap)) {
            for (Map.Entry<String, Collection<Long>> entry : roleIdMenuIdMap.entrySet()) {
                List<ManagerMenu> managerMenus = Lists.newArrayList();
                for (Long mid : entry.getValue()) {
                    managerMenus.add(tmpMap.get(U.toStr(mid)));
                }
                returnMap.put(entry.getKey(), managerMenus);
            }
        }
        return returnMap;
    }

    /** 将有层级关系的菜单平级返回 */
    static List<ManagerMenu> handleAllMenu(List<ManagerMenu> menus) {
        if (A.isEmpty(menus)) {
            return Collections.emptyList();
        } else {
            List<ManagerMenu> returnList = Lists.newArrayList();
            for (ManagerMenu menu : menus) {
                allMenu(returnList, menu);
            }
            return returnList;
        }
    }
    private static void allMenu(List<ManagerMenu> returnList, ManagerMenu menu) {
        if (U.isNotBlank(menu)) {
            returnList.add(menu);
        }
        if (A.isNotEmpty(menu.getChildMenus())) {
            for (ManagerMenu childMenu : menu.getChildMenus()) {
                allMenu(returnList, childMenu);
            }
        }
    }
}
