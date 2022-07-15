package com.xiaoyaowind.util;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.xiaoyaowind.model.DataSourceInfo;
import java.io.File;
import java.util.ArrayList;

public class GenerateCodeUtil {

    public static void generateCodeInfo(DataSourceInfo dataSourceInfo){
        AutoGenerator mpg = new AutoGenerator();
        // 配置策略
        // 1、全局配置
        GlobalConfig gc = setGlobalConfig(dataSourceInfo);
        // 2、包配置
        mpg.setGlobalConfig(setExtendName(gc));
        // 3、设置数据源
        mpg.setDataSource(setDataSource(dataSourceInfo));
        // 4、包的配置
        mpg.setPackageInfo(setPackege(""));
        // 5、策略配置
        mpg.setStrategy(setStrategy(dataSourceInfo.getTables(),dataSourceInfo.isNormalize()));
        // 6、模板引擎配置
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setTemplate(setTemplateConfig(dataSourceInfo.getTemplatePath()));
        // 7、数据执行
        mpg.execute(); //执行
        //将resource移动到对应地点中，进行覆盖处理
        FileUtil.move(new File(dataSourceInfo.getClassPath()+"\\"+""+"\\mapping\\"),new File(dataSourceInfo.getResourcePath()+"\\mapping\\"),true);
    }

    private static StrategyConfig setStrategy(String tables, boolean isNormalize) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tables.split(",")); // 需要生成的表 设置要映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); // 自动lombok；
        strategy.setCapitalMode(false); //【不懂】 开启全局大写命名
        strategy.setSuperMapperClass(null); //【不懂】
        // 是否需要开启特定规范字段
        if (true == isNormalize) {
            strategy.setLogicDeleteFieldName("deleted");
            // 自动填充配置
            TableFill gmtCreate = new TableFill("create_time", FieldFill.INSERT);
            TableFill gmtModified = new TableFill("op_time",
                    FieldFill.INSERT_UPDATE);
            TableFill gmtModifiedNew = new TableFill("update_time",
                    FieldFill.INSERT_UPDATE);
            ArrayList<TableFill> tableFills = new ArrayList<>();
            tableFills.add(gmtCreate);
            tableFills.add(gmtModified);
            tableFills.add(gmtModifiedNew);
            strategy.setTableFillList(tableFills);
            // 乐观锁
            strategy.setVersionFieldName("version");
        }
        strategy.setRestControllerStyle(true); // 控制：true——生成@RsetController false——生成@Controller
        strategy.setControllerMappingHyphenStyle(true); // 【不知道是啥】
        strategy.setEntityTableFieldAnnotationEnable(true); // 表字段注释启动 启动模板中的这个 <#if table.convert>
        strategy.setEntityBooleanColumnRemoveIsPrefix(true); // 是否删除实体类字段的前缀
        strategy.setControllerMappingHyphenStyle(false); // 控制层mapping的映射地址 false：infRecData true：inf_rec_data
        return strategy;
    }


    private static PackageConfig setPackege(String packageParent) {
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageParent);
        pc.setController("controller"); // 可以不用设置，默认是这个
        pc.setService("service"); // 同上
        pc.setServiceImpl("model"  + ".dto"); // 同上
        pc.setMapper("dao"); // 默认是mapper
        pc.setEntity("entity"); // 默认是entity
        pc.setXml("mapping"); // 默认是默认是mapper.xml
        return pc;
    }

    private static TemplateConfig setTemplateConfig(String path) {
        TemplateConfig tc = new TemplateConfig();
        tc.setController(path+"/controller.java");
        tc.setService(path+"/service.java");
        tc.setServiceImpl(path + "/dto.java"); //hack for dto
        tc.setEntity(path+"/entity.java");
        tc.setMapper(path+"/mapper.java");
        tc.setXml(path+"/mapper.xml");
        return tc;
    }

    private static GlobalConfig setExtendName(GlobalConfig gc) {
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setFileOverride(true);
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sDTO");
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        return gc;
    }

    private static DataSourceConfig setDataSource(DataSourceInfo dataSourceInfo) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(dataSourceInfo.getUrl());
        dsc.setDriverName(dataSourceInfo.getDriver());
        dsc.setUsername(dataSourceInfo.getUserName());
        dsc.setPassword(dataSourceInfo.getPassword());
        return dsc;
    }

    private static GlobalConfig setGlobalConfig(DataSourceInfo dataSourceInfo) {
        GlobalConfig gc=new GlobalConfig();
        gc.setOutputDir(dataSourceInfo.getClassPath());// 生成文件输出根目录
        gc.setAuthor(System.getenv().get("USERNAME"));// 作者
        gc.setOpen(false); // 生成完成后不弹出文件框
        gc.setFileOverride(true); // 文件是否覆盖
        gc.setIdType(IdType.ASSIGN_ID); //主键策略 实体类主键ID类型
        // gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true); // 是否开启swagger
        gc.setActiveRecord(true); //【不懂】 活动记录 不需要ActiveRecord特性的请改为false 是否支持AR模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);//【不懂】 XML ResultMap xml映射文件的配置
        gc.setBaseColumnList(false);//【不懂】 XML columList xml映射文件的配置
        return gc;
    }
}
