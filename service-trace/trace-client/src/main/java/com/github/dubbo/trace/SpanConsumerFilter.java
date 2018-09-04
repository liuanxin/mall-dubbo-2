//package com.github.dubbo.trace;
//
//import com.alibaba.dubbo.common.Constants;
//import com.alibaba.dubbo.common.extension.Activate;
//import com.alibaba.dubbo.rpc.*;
//import org.springframework.cloud.sleuth.Span;
//import org.springframework.cloud.sleuth.SpanInjector;
//import org.springframework.cloud.sleuth.Tracer;
//import org.springframework.core.Ordered;
//
//@Activate(group = Constants.CONSUMER, order = Ordered.HIGHEST_PRECEDENCE)
//public class SpanConsumerFilter implements Filter {
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        Tracer tracer = ApplicationContextUtils.getBean(Tracer.class);
//        SpanInjector<RpcContext> spanInjector = ApplicationContextUtils.getBean(DubboSpanInjector.class);
//
//        boolean isTrace = (tracer != null && spanInjector != null);
//        if (isTrace) {
//            try {
//                String spanName = TraceUtil.createSpanName(invoker, invocation);
//                Span newSpan = tracer.createSpan(spanName);
//                spanInjector.inject(newSpan, RpcContext.getContext());
//                newSpan.logEvent(Span.CLIENT_SEND);
//            } finally {
//                if (tracer.isTracing()) {
//                    tracer.getCurrentSpan().logEvent(Span.CLIENT_RECV);
//                    tracer.close(tracer.getCurrentSpan());
//                }
//            }
//        }
//        return invoker.invoke(invocation);
//    }
//}
