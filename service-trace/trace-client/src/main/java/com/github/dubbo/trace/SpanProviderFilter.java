//package com.github.dubbo.trace;
//
//import com.alibaba.dubbo.common.Constants;
//import com.alibaba.dubbo.common.extension.Activate;
//import com.alibaba.dubbo.rpc.*;
//import org.springframework.cloud.sleuth.*;
//import org.springframework.cloud.sleuth.sampler.NeverSampler;
//import org.springframework.core.Ordered;
//
//import java.util.Map;
//
//@Activate(group = Constants.PROVIDER, order = Ordered.HIGHEST_PRECEDENCE)
//public class SpanProviderFilter implements Filter {
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        Map<String, String> attachments = RpcContext.getContext().getAttachments();
//        Tracer tracer = ApplicationContextUtils.getBean(Tracer.class);
//        SpanExtractor<RpcContext> spanExtractor = ApplicationContextUtils.getBean(DubboSpanExtractor.class);
//        SpanInjector<RpcContext> spanInjector = ApplicationContextUtils.getBean(DubboSpanInjector.class);
//        SpanReporter spanReporter = ApplicationContextUtils.getBean(SpanReporter.class);
//
//        boolean isTrace = (tracer != null && spanExtractor != null && spanInjector != null && spanReporter != null);
//        if (isTrace) {
//            Span span = null;
//            try {
//                String spanName = TraceUtil.createSpanName(invoker, invocation);
//                Span parent = spanExtractor.joinTrace(RpcContext.getContext());
//                if (parent != null) {
//                    span = tracer.createSpan(spanName, parent);
//                    if (parent.isRemote()) {
//                        parent.logEvent(Span.SERVER_RECV);
//                    }
//                } else {
//                    boolean skip = Span.SPAN_NOT_SAMPLED.equals(attachments.get(Span.SAMPLED_NAME));
//                    if (skip) {
//                        span = tracer.createSpan(spanName, NeverSampler.INSTANCE);
//                    } else {
//                        span = tracer.createSpan(spanName);
//                    }
//                    span.logEvent(Span.SERVER_RECV);
//                }
//                spanInjector.inject(span, RpcContext.getContext());
//            } finally {
//                if (span != null) {
//                    if (span.hasSavedSpan()) {
//                        Span parent = span.getSavedSpan();
//                        if (parent.isRemote()) {
//                            parent.logEvent(Span.SERVER_SEND);
//                            parent.stop();
//                            spanReporter.report(parent);
//                        }
//                    } else {
//                        span.logEvent(Span.SERVER_SEND);
//                    }
//                    tracer.close(span);
//                }
//            }
//        }
//        return invoker.invoke(invocation);
//    }
//}
