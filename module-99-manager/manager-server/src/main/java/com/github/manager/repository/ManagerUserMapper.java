package com.github.manager.repository;

import com.github.liuanxin.page.model.PageBounds;
import com.github.manager.model.ManagerUser;
import com.github.manager.model.ManagerUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerUserMapper {
    int countByExample(ManagerUserExample example);

    int deleteByExample(ManagerUserExample example);

    int deleteByPrimaryKey(Long id);

    int insertSelective(ManagerUser record);

    List<ManagerUser> selectByExample(ManagerUserExample example, PageBounds page);

    List<ManagerUser> selectByExample(ManagerUserExample example);

    ManagerUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ManagerUser record, @Param("example") ManagerUserExample example);

    int updateByPrimaryKeySelective(ManagerUser record);

    int batchInsert(@Param("list") List<ManagerUser> list);
}
