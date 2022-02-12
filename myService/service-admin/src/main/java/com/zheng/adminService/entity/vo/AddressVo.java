package com.zheng.adminService.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "地名")
    private String addressName;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "县")
    private String county;

    @ApiModelProperty(value = "纬度")
    private String lag;

    @ApiModelProperty(value = "经度")
    private String lat;

    @ApiModelProperty(value = "介绍")
    private String detailed;

    @ApiModelProperty(value = "是否删除 0：未删除 1：已删除")
    private Integer isDelete;
}
