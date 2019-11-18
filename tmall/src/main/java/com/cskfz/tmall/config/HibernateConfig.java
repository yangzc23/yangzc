package com.cskfz.tmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 16:04 2019/11/16
 * @Modified By:
 */
@Configuration
public class HibernateConfig {

    @Autowired
    private DataSource dataSource;


    @Autowired
    private ResourceLoader rl;

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan("com.cskfz.tmall.entity");//dao和entity的公共包
        sessionFactoryBean.setMappingLocations(loadResources());
        //sessionFactoryBean.setMappingResources("/sqlMapperXml/TItem.hbm.xml","/sqlMapperXml/TOrder.hbm.xml","/sqlMapperXml/TProduct.hbm.xml","/sqlMapperXml/TUser.hbm.xml");
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty("hibernate.show_sql","true");
        properties.setProperty("hibernate.hbm2ddl.auto","update");
        sessionFactoryBean.setHibernateProperties(properties);
        return sessionFactoryBean;
    }

    public Resource[] loadResources() {
        Resource[] resources = null;
        try {
            resources = ResourcePatternUtils.getResourcePatternResolver(rl).getResources("classpath:/sqlMapperXml/*.hbm.xml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resources;
    }
}
