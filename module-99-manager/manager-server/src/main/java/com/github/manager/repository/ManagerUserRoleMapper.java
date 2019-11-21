package com.github.manager.repository;

import com.github.liuanxin.page.model.PageBounds;
import com.github.manager.model.ManagerUserRole;
import com.github.manager.model.ManagerUserRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerUserRoleMapper {
    int countByExample(ManagerUserRoleExample example);

    int deleteByExample(ManagerUserRoleExample example);

    int insertSelective(ManagerUserRole record);

    List<ManagerUserRole> selectByExample(ManagerUserRoleExample example, PageBounds page);

    List<ManagerUserRole> selectByExample(ManagerUserRoleExample example);

    int updateByExampleSelective(@Param("record") ManagerUserRole record, @Param("example") ManagerUserRoleExample example);

    int batchInsert(@Param("list") List<ManagerUserRole> list);
}
