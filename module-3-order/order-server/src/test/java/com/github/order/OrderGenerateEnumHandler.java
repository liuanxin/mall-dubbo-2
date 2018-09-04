package com.github.order;

import com.github.common.Const;
import com.github.common.util.GenerateEnumHandler;
import com.github.order.constant.OrderConst;
import org.junit.Test;

public class OrderGenerateEnumHandler {

    @Test
    public void generate() {
        GenerateEnumHandler.generateEnum(getClass(), Const.BASE_PACKAGE, OrderConst.MODULE_NAME);
    }
}
