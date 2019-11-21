package com.github.manager.config;

import com.github.common.resource.CollectResourceUtil;
import com.github.common.resource.CollectTypeHandlerUtil;
import com.github.common.util.A;
import com.github.global.constant.GlobalConst;
import com.github.manager.constant.ManagerConst;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.core.io.Resource;

/** 管理模块的配置数据. 主要是 mybatis 的多配置目录和类型处理器 */
final class ManagerConfigData {

    private static final String[] RESOURCE_PATH = new String[] {
            ManagerConst.MODULE_NAME + "/*.xml",
            ManagerConst.MODULE_NAME + "-custom/*.xml"
    };
    /** 要加载的 mybatis 的配置文件目录 */
    static final Resource[] RESOURCE_ARRAY = CollectResourceUtil.resource(A.maps(
            ManagerConfigData.class, RESOURCE_PATH
    ));
    
    /** 要加载的 mybatis 类型处理器的目录 */
    static final TypeHandler[] HANDLER_ARRAY = CollectTypeHandlerUtil.typeHandler(A.maps(
            GlobalConst.MODULE_NAME, GlobalConst.class,
            ManagerConst.MODULE_NAME, ManagerConfigData.class
    ));
}
