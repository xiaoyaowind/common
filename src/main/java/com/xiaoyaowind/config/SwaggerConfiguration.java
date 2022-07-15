package com.xiaoyaowind.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.List;

/**
 * swagger配置类
 * 浏览器访问地址：ip+port/doc.html
 * @author cat
 * @Date 2022/07/15
 */
@Configuration
@Profile({"dev"})
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    protected final OpenApiExtensionResolver openApiExtensionResolver;

    @Value("${swagger.groupName:default}")
    private String groupName;

    @Value("${swagger.basePackage:com.xiaoyaowind}")
    private String basePackage;
    @Value("${swagger.title:xiaoyaowind-接口文档}")
    private String title;
    @Value("${swagger.description:com.xiaoyaowind}")
    private String description;
    @Value("${swagger.version:1.0}")
    private String version;
    @Autowired
    public SwaggerConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        Docket docket= new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .groupName(groupName)
                .directModelSubstitute(LocalTime.class, String.class)
                .select()
                // 为指定包下的controller生成API文档
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildSettingExtensions())
                // 可以存在多个apiKey()，用于全局权限身份校验
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()));
        return docket;
    }

    /**
     * 文档信息
     * @return
     */
    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }

    /**
     * 如果没有使用检验Token的方式做身份验证，那么后面这三个方法都不需要
     * 用于全局身份验证，当使用JWT时，需要前端传递Token
     * 可以存在多个ApiKey
     * @return
     */
    protected ApiKey apiKey() {
        return new ApiKey("Token", "Authorization", "header");
    }

    protected SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }
}