package com.zheng.adminService.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DialectVideoVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "������id")
    private String addressId;

    @ApiModelProperty(value = "��Ƶ����")
    private String name;

    @ApiModelProperty(value = "Ȥζ��Ƶid")
    private String videoId;

    @ApiModelProperty(value = "��Ƶ����")
    private String introduce;

}
