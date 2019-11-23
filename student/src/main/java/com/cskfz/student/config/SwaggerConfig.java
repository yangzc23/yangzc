package com.cskfz.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 16:47 2019/11/23
 * @Modified By:
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"com.cskfz.student.controller"})
public class SwaggerConfig {
    @Bean
    public Docket customDocket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo());
        return docket;
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("yangzc","https://github.com/yangzc23/","876295854@qq.com");
        return new ApiInfoBuilder().
                title("学生管理API").
                description("学生管理接口说明文档").
                contact(contact).
                version("1.0.0").
                build();
    }

}
