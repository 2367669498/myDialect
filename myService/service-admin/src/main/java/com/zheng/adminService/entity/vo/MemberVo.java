package com.zheng.adminService.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "�˺�")
    private String input;

    @ApiModelProperty(value = "����")
    private String username;

    @ApiModelProperty(value = "�ֻ���")
    private String phone;
    
    @ApiModelProperty(value = "����")
    private String password;

    @ApiModelProperty(value = "ͷ��")
    private String avater;



}
