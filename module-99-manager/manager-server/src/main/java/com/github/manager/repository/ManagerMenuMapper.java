package com.github.manager.repository;

import com.github.liuanxin.page.model.PageBounds;
import com.github.manager.model.ManagerMenu;
import com.github.manager.model.ManagerMenuExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerMenuMapper {
    int countByExample(ManagerMenuExample example);

    int deleteByExample(ManagerMenuExample example);

    int deleteByPrimaryKey(Long id);

    int insertSelective(ManagerMenu record);

    List<ManagerMenu> selectByExample(ManagerMenuExample example, PageBounds page);

    List<ManagerMenu> selectByExample(ManagerMenuExample example);

    ManagerMenu selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ManagerMenu record, @Param("example") ManagerMenuExample example);

    int updateByPrimaryKeySelective(ManagerMenu record);

    int batchInsert(@Param("list") List<ManagerMenu> list);
}
