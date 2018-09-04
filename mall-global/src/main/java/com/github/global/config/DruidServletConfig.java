package com.github.global.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

@Configuration
@ConditionalOnClass({ Servlet.class, DruidDataSource.class })
public class DruidServletConfig {

    private static final String URL_MAPPING = "/druid/*";
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "admin";

    /** 非线上环境时开启 druid 检测 */
    @Bean
    @ConditionalOnProperty(name = "online", havingValue = "false")
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletBean = new ServletRegistrationBean<>(new StatViewServlet(), URL_MAPPING);
        servletBean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME, USER_NAME);
        servletBean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD, PASSWORD);
        return servletBean;
    }
}
