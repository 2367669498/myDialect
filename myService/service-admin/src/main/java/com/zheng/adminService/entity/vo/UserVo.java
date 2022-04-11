package com.zheng.adminService.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVo {
    private String id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户密码")
    private String pwd;

    @ApiModelProperty(value = "用户转态  1：启用 2：禁用")
    private Integer status;

    @ApiModelProperty(value = "是否删除  1：删除 0：未删除")
    private Integer isDelete;
}
