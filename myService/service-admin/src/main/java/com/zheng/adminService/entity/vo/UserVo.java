package com.zheng.adminService.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVo {
    private String id;

    @ApiModelProperty(value = "�û�����")
    private String name;

    @ApiModelProperty(value = "�û�����")
    private String pwd;

    @ApiModelProperty(value = "�û�ת̬  1������ 2������")
    private Integer status;

    @ApiModelProperty(value = "�Ƿ�ɾ��  1��ɾ�� 0��δɾ��")
    private Integer isDelete;
}
