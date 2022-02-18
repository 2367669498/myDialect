package com.zheng.adminService.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DialectRegionalVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "乡音的名称")
    private String regionalName;

    @ApiModelProperty(value = "乡音采集样式 1：单字  2：词汇  3：例句  4：民谣")
    private Integer regionalType;

    @ApiModelProperty(value = "是否存在视频 0：不存在 1：存在")
    private Integer isVideo;

    @ApiModelProperty(value = "乡音视频保存文件id")
    private String videoId;

    @ApiModelProperty(value = "是否存在音频 0：不存在 1：存在")
    private Integer isMusic;

    @ApiModelProperty(value = "乡音音频保存文件id")
    private String musicId;

    @ApiModelProperty(value = "归属地id")
    private String addressId;

    @ApiModelProperty(value = "归属地")
    private String addressName;

    @ApiModelProperty(value = "乡音归属地说明")
    private String addressDetailed;

    @ApiModelProperty(value = "详细说明")
    private String detailed;

    @ApiModelProperty(value = "上传者id")
    private String uploadUserId;

    @ApiModelProperty(value = "状态：1：审核中 2：以通过 3：禁用")
    private Integer status;
}
