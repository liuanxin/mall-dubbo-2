package com.github.manager.repository;

import com.github.liuanxin.page.model.PageBounds;
import com.github.manager.model.ManagerPermission;
import com.github.manager.model.ManagerPermissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerPermissionMapper {
    int countByExample(ManagerPermissionExample example);

    int deleteByExample(ManagerPermissionExample example);

    int deleteByPrimaryKey(Long id);

    int insertSelective(ManagerPermission record);

    List<ManagerPermission> selectByExample(ManagerPermissionExample example, PageBounds page);

    List<ManagerPermission> selectByExample(ManagerPermissionExample example);

    ManagerPermission selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ManagerPermission record, @Param("example") ManagerPermissionExample example);

    int updateByPrimaryKeySelective(ManagerPermission record);

    int batchInsert(@Param("list") List<ManagerPermission> list);
}
