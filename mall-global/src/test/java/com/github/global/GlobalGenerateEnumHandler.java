package com.github.global;

import com.github.common.Const;
import com.github.common.util.GenerateEnumHandler;
import com.github.global.constant.GlobalConst;
import org.junit.Test;

public class GlobalGenerateEnumHandler {

    @Test
    public void generate() {
        GenerateEnumHandler.generateEnum(getClass(), Const.BASE_PACKAGE, GlobalConst.MODULE_NAME);
    }
}
