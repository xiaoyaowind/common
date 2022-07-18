package com.xiaoyaowind.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoyaowind.common.model.DataSourceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class PathUtil {
    //获取classPath路径(E://foc/cao/umax/foc这样的)
    private static String getProjectRootPath(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                if ( file.getAbsolutePath().contains("Application")) {
                    String all = file.getAbsolutePath().replaceAll("\\\\", "/");
                    String rm = all.substring(all.lastIndexOf("/"));
                    String s = all.replaceAll(rm, "");
                    return s;
                } else {
                    String files1 = getProjectRootPath(file.getAbsolutePath());
                    if (StrUtil.isNotEmpty(files1)) {
                        return files1;
                    }
                }
            } else {
                if ( file.getAbsolutePath().contains("Application")) {
                    String all = file.getAbsolutePath().replaceAll("\\\\", "/");

                    int idx = all.lastIndexOf("/");

                    String rm = all.substring(idx);
                    String s = all.replaceAll(rm, "");
                    return s;
                }
            }
        }
        return null;
    }


    private static String getResourcePath(String path) {
        String result = path.substring(0, path.indexOf("/src/main") + 9) + "/resources";
        return result;
    }

    public static void getPath(ConfigurableApplicationContext run) {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("/target/classes", "");
        DataSourceInfo dataSourceInfo = run.getBean(DataSourceInfo.class);
        String rootPath;
        if(StrUtil.isNotEmpty(dataSourceInfo.getModuleName())){
            path=System.getProperty("user.dir")+"\\"+dataSourceInfo.getModuleName();
            rootPath=getModuleRoutePath(path);
        }else{
            rootPath = getProjectRootPath(path);
        }
        if (StrUtil.isEmpty(dataSourceInfo.getUrl())) {
            String url = run.getEnvironment().getProperty("spring.datasource.url");
            dataSourceInfo.setUrl(url);
        }
        if (StrUtil.isEmpty(dataSourceInfo.getUserName())) {
            String username = run.getEnvironment().getProperty("spring.datasource.username");
            dataSourceInfo.setUserName(username);
        }
        if (StrUtil.isEmpty(dataSourceInfo.getPassword())) {
            String password = run.getEnvironment().getProperty("spring.datasource.password");
            dataSourceInfo.setPassword(password);
        }
        if (StrUtil.isEmpty(dataSourceInfo.getDriver())) {
            String driver = "com.mysql.cj.jdbc.Driver";
            dataSourceInfo.setDriver(driver);
        }
        if (Objects.isNull(dataSourceInfo.isNormalize())) {
            dataSourceInfo.setNormalize(true);
        }
        log.info("用户名:[{}],url:[{}]", dataSourceInfo.getUserName(), dataSourceInfo.getUrl());

        if (StrUtil.isEmpty(dataSourceInfo.getTables())) {
            log.error("导入表数据时必须选择一张表");
        }
        dataSourceInfo.setPackageName(getPackageName(rootPath));
        dataSourceInfo.setClassPath(getClassPath(rootPath,dataSourceInfo.getPackageName()));
        dataSourceInfo.setResourcePath(getResourcePath(rootPath));
        try {
            GenerateCodeUtil.generateCodeInfo(dataSourceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getClassPath(String rootPath, String packageName){
       return rootPath.replaceAll(packageName,"");
    }

    private static String getModuleRoutePath(String path) {
        File root = new File(path);
        File[] files = root.listFiles();
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                if ( file.getAbsolutePath().contains("controller")) {
                    String all = file.getAbsolutePath().replaceAll("\\\\", "/");
                    String rm = all.substring(all.lastIndexOf("/"));
                    String s = all.replaceAll(rm, "");
                    return s;
                } else {
                    String files1 = getModuleRoutePath(file.getAbsolutePath());
                    if (StrUtil.isNotEmpty(files1)) {
                        return files1;
                    }
                }
            } else {
                if ( file.getAbsolutePath().contains("controller")) {
                    String all = file.getAbsolutePath().replaceAll("\\\\", "/");
                    int idx = all.lastIndexOf("/");
                    String rm = all.substring(idx);
                    String s = all.replaceAll(rm, "");
                    return s;
                }
            }
        }

        //第一次生成文件,需要特殊处理
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                if ( file.getAbsolutePath().contains("java")&& CollUtil.isEmpty(Arrays.asList(file.listFiles()))) {
                    String moduleRoutePath = getModuleRoutePath(file.getAbsolutePath());
                    if (StrUtil.isEmpty(moduleRoutePath)) {
                        moduleRoutePath = file.getAbsolutePath().replaceAll("\\\\", "/");

                        return moduleRoutePath;
                    }
                }
            }
        }
        return null;
    }

    private static String getPackageName(String path) {
        String result;
        try {
            result = path.substring(path.indexOf("/src/main/java") + 15);
        } catch (Exception e) {
            return "";
        }
        return result;
    }
}
