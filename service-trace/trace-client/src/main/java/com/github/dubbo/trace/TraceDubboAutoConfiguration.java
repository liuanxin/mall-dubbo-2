//package com.github.dubbo.trace;
//
//import com.alibaba.dubbo.rpc.Filter;
//import com.alibaba.dubbo.rpc.RpcContext;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.cloud.sleuth.SpanExtractor;
//import org.springframework.cloud.sleuth.SpanInjector;
//import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Random;
//
//@Configuration
//@ConditionalOnProperty(value = "dubbo.trace.enabled", matchIfMissing = true)
//@ConditionalOnClass(Filter.class)
//@AutoConfigureAfter(TraceAutoConfiguration.class)
//public class TraceDubboAutoConfiguration {
//
//    @Bean
//    @ConditionalOnMissingBean
//    public ApplicationContextUtils applicationContextAwareBean() {
//        return new ApplicationContextUtils();
//    }
//
//    @Bean
//    public SpanInjector<RpcContext> spanInjector() {
//        return new DubboSpanInjector();
//    }
//
//    /** @see TraceAutoConfiguration#randomForSpanIds() */
//    @Bean
//    public SpanExtractor<RpcContext> spanExtractor(Random random) {
//        return new DubboSpanExtractor(random);
//    }
//}
