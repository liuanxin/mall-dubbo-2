package com.github.message;

import com.github.common.Const;
import com.github.common.util.GenerateEnumHandler;
import com.github.message.constant.QueueConst;
import org.junit.Test;

public class QueueGenerateEnumHandler {

    @Test
    public void generate() {
        GenerateEnumHandler.generateEnum(getClass(), Const.BASE_PACKAGE, QueueConst.MODULE_NAME);
    }
}
