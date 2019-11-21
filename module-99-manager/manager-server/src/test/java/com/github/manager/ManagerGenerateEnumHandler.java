package com.github.manager;

import com.github.common.Const;
import com.github.common.util.GenerateEnumHandler;
import com.github.manager.constant.ManagerConst;
import org.junit.Test;

public class ManagerGenerateEnumHandler {

    @Test
    public void generate() {
        GenerateEnumHandler.generateEnum(getClass(), Const.BASE_PACKAGE, ManagerConst.MODULE_NAME);
    }
}
