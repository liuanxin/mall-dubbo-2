package com.github.search;

import com.github.common.Const;
import com.github.common.util.GenerateEnumHandler;
import com.github.search.constant.SearchConst;
import org.junit.Test;

public class SearchGenerateEnumHandler {

    @Test
    public void generate() {
        GenerateEnumHandler.generateEnum(getClass(), Const.BASE_PACKAGE, SearchConst.MODULE_NAME);
    }
}
