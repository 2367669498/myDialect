package com.zheng.adminService.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DialectVideoVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "归属地id")
    private String addressId;

    @ApiModelProperty(value = "视频名称")
    private String name;

    @ApiModelProperty(value = "趣味视频id")
    private String videoId;

    @ApiModelProperty(value = "视频介绍")
    private String introduce;

}
