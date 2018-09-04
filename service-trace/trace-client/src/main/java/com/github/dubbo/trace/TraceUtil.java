package com.github.dubbo.trace;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

final class TraceUtil {

    private static final String INTERFACE = "interface";
    private static final String VERSION = "version";

    static String createSpanName(Invoker<?> invoker, Invocation invocation) {
        URL url = invoker.getUrl();
        return String.format("%s:%s:%s(%s)", url.getParameter(INTERFACE),
                invocation.getMethodName(), url.getParameter(VERSION), url.getHost());
    }
}
