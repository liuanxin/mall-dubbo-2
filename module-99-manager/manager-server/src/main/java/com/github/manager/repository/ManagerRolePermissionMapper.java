package com.github.manager.repository;

import com.github.liuanxin.page.model.PageBounds;
import com.github.manager.model.ManagerRolePermission;
import com.github.manager.model.ManagerRolePermissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerRolePermissionMapper {
    int countByExample(ManagerRolePermissionExample example);

    int deleteByExample(ManagerRolePermissionExample example);

    int insertSelective(ManagerRolePermission record);

    List<ManagerRolePermission> selectByExample(ManagerRolePermissionExample example, PageBounds page);

    List<ManagerRolePermission> selectByExample(ManagerRolePermissionExample example);

    int updateByExampleSelective(@Param("record") ManagerRolePermission record, @Param("example") ManagerRolePermissionExample example);

    int batchInsert(@Param("list") List<ManagerRolePermission> list);
}
