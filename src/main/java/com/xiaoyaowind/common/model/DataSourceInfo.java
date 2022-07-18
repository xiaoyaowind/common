package com.xiaoyaowind.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Data
@Component
@ConfigurationProperties(prefix = "generate")
public class DataSourceInfo {

    @ApiModelProperty("数据库url")
    private String url;

    @ApiModelProperty("数据库驱动类")
    private String driver;

    @ApiModelProperty("数据库用户名")
    private String userName;

    @ApiModelProperty("数据库密码")
    private String password;

    @ApiModelProperty("要生成的表(用,拼接)")
    private String tables;

    @ApiModelProperty("要生成的模板所在路径")
    private String templatePath="/templates";

    @ApiModelProperty("要生成的模块")
    private String moduleName;

   @ApiModelProperty("要生成的类路径")
    private String classPath;

    @ApiModelProperty("要生成的mapper路径")
    private String resourcePath="/resources";

    @ApiModelProperty("否需要开启特定规范字段")
    private boolean normalize;

    @ApiModelProperty("要生成的包名称")
    private String packageName;
}
