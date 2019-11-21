package com.github.manager.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.common.Const;
import com.github.liuanxin.page.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 扫描指定目录. MapperScan 的处理类是 MapperScannerRegistrar, 其基于 ClassPathMapperScanner<br>
 *
 * @see org.mybatis.spring.annotation.MapperScannerRegistrar#registerBeanDefinitions
 * @see org.mybatis.spring.mapper.MapperScannerConfigurer#postProcessBeanDefinitionRegistry
 * @see org.mybatis.spring.mapper.ClassPathMapperScanner
 */
@Configuration
@EnableCaching
@MapperScan(basePackages = Const.BASE_PACKAGE)
public class ManagerDataSourceInit {

    /** mybatis 的分页插件 */
    private Interceptor mybatisPage() {
        return new PageInterceptor("mysql");
    }

    /**
     * http://docs.spring.io/spring-boot/docs/1.3.6.RELEASE/reference/htmlsingle/#howto-two-datasources<br/>
     * &#064;EnableTransactionManagement 注解等同于: &lt;tx:annotation-driven /&gt;
     *
     * @see org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
     * @see org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
     */
    @Bean
    @ConfigurationProperties(prefix = "database")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        // 装载 xml 实现
        sessionFactory.setMapperLocations(ManagerConfigData.RESOURCE_ARRAY);
        // 装载 typeHandler 实现
        sessionFactory.setTypeHandlers(ManagerConfigData.HANDLER_ARRAY);
        // 插件
        sessionFactory.setPlugins(mybatisPage());
        return sessionFactory.getObject();
    }

    /** 要构建 or 语句, 参考: http://www.mybatis.org/generator/generatedobjects/exampleClassUsage.html */
    @Bean(name = "sqlSessionTemplate", destroyMethod = "clearCache")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /*
     * 事务控制, 默认已经装载了
     *
     * @see DataSourceTransactionManagerAutoConfiguration
     */
    /*
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    */
}
