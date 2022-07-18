package ${package.Controller};

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyaowind.common.model.PageResult;
import com.xiaoyaowind.common.model.Result;
import ${package.ServiceImpl}.${entity}DTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.collection.CollectionUtil;
import java.util.List;
import java.util.ArrayList;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
* <p>
* ${table.comment!} 前端控制器
* </p>
*
* @author ${author}
* @since ${date}
*/

@Slf4j
@Api(tags = "${table.comment!}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
    class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
        public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
        public class ${table.controllerName} {

        @Autowired
        private ${table.serviceName} ${table.entityPath}Service;


        @ApiOperation(value = "新增")
        @PostMapping("")
        public Result save(@RequestBody ${entity}DTO ${table.entityPath}DTO){
        var ${table.entityPath} = BeanUtil.copyProperties(${table.entityPath}DTO, ${entity}.class);
        ${table.entityPath}Service.save(${table.entityPath});
        return Result.success();
        }

        @ApiOperation(value = "根据id删除")
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable("id") String id){
        ${table.entityPath}Service.removeById(id);
        return Result.success();
        }

        @ApiOperation(value = "根据id修改")
        @PutMapping("/{id}")
        public Result update(@PathVariable("id") String id, @RequestBody ${entity}DTO ${table.entityPath}DTO){
        var ${table.entityPath} = BeanUtil.copyProperties(${table.entityPath}DTO, ${entity}.class);
        ${table.entityPath}Service.updateById(${table.entityPath});
        return Result.success();
        }

        /*
        @ApiOperation(value = "条件查询")
        @PostMapping("/pageQuery")
        public Result<List<${table.serviceImplName}>> list(@RequestBody ${entity} ${table.entityPath}){
        List<${entity}> ${table.entityPath}List = ${table.entityPath}Service.list(new QueryWrapper<>(${table.entityPath}));
        return Result.success(${table.entityPath}List);
        }*/

        @ApiOperation(value = "查询所有")
        @GetMapping("")
        public Result<List<${table.serviceImplName}>> list() {
        var ${table.entityPath}List = ${table.entityPath}Service.list(new QueryWrapper<>());
        var ${table.entityPath}DTOList = BeanUtil.copyToList(${table.entityPath}List, ${entity}DTO.class);
        return Result.success(${table.entityPath}DTOList);
        }

        @ApiOperation(value = "列表（分页）")
        @GetMapping("/list")
        public Result<PageResult<${table.serviceImplName}>> list(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize){
        var page = ${table.entityPath}Service.page(new Page<>(pageNum, pageSize), null);
        return Result.success(new PageResult<>(page.getTotal(),page.getRecords()));
        }

        @ApiOperation(value = "详情")
        @GetMapping("/{id}")
        public Result<${table.serviceImplName}> get(@PathVariable("id") String id){
        var ${table.entityPath}= ${table.entityPath}Service.getById(id);
        if ( ${table.entityPath}  != null) {
        var ${table.entityPath}DTO = BeanUtil.copyProperties(${table.entityPath}, ${entity}DTO.class);
        return Result.success(${table.entityPath}DTO);
        }
        return Result.fail(404,"找不到实体,id:"+ id);
        }
    </#if>
    }
</#if>