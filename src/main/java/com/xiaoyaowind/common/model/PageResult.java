package com.xiaoyaowind.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: cat
 * @time: 20210601
 */
@AllArgsConstructor
@Data
@Builder
@ApiModel("分页数据")
public class PageResult <T>{

    @ApiModelProperty("总数")
    private long total;

    @ApiModelProperty("当前页数据")
    private List<T> rows;

}