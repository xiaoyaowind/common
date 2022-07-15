package ${package.Service};

import org.springframework.stereotype.Service;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${superServiceImplClassPackage};

/**
* <p>
    * ${table.comment!} 服务类
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
    class ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
@Service
public class ${table.serviceName} extends  ${superServiceImplClass}<${table.mapperName}, ${entity}> {

}
</#if>