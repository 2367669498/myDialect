package com.zheng.adminService.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.Serializable;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DialectRegional对象", description="乡音采集模块")

public class DialectRegional implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "乡音的名称")
    private String regionalName;

    @ApiModelProperty(value = "乡音的类型  0：未知 1：音频 2：视频")
    private Integer type;

    @ApiModelProperty(value = "乡音采集样式 1：单字  2：词汇  3：例句  4：民谣")
    private Integer regionalType;

    @ApiModelProperty(value = "乡音文件保存的地址")
    private String videoUrl;

    @ApiModelProperty(value = "文件id")
    private String fileId;

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

    @ApiModelProperty(value = "是否删除 0：未删除 1：已删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
