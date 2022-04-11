package com.zheng.adminService.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "账号")
    private String input;

    @ApiModelProperty(value = "名称")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String phone;
    
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String avater;



}
