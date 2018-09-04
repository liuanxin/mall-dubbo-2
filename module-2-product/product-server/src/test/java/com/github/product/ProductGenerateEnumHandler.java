package com.github.product;

import com.github.common.Const;
import com.github.common.util.GenerateEnumHandler;
import com.github.product.constant.ProductConst;
import org.junit.Test;

public class ProductGenerateEnumHandler {

    @Test
    public void generate() {
        GenerateEnumHandler.generateEnum(getClass(), Const.BASE_PACKAGE, ProductConst.MODULE_NAME);
    }
}
