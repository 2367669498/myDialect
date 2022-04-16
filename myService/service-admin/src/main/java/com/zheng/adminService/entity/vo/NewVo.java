package com.zheng.adminService.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String newName;

    @ApiModelProperty(value = "区域id")
    private String regionalId;


    @ApiModelProperty(value = "内容")
    private String detailed;

    @ApiModelProperty(value = "图片路径")
    private String imgurl;

    @ApiModelProperty(value = "上传者id")
    private String userId;

    @ApiModelProperty(value = "token")
    private String token;
}
