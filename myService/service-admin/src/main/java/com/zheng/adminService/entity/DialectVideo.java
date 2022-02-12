package com.zheng.adminService.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 趣味视频表
 * </p>
 *
 * @author zhengdi
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DialectVideo对象", description="趣味视频表")
public class DialectVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "归属地id")
    private String addressId;

    @ApiModelProperty(value = "视频名称")
    private String name;

    @ApiModelProperty(value = "趣味视频地址")
    private String videoUrl;

    @ApiModelProperty(value = "趣味视频id")
    private String videoId;

    @ApiModelProperty(value = "视频介绍")
    private String introduce;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除 0：未删除 1：已删除")
    private Integer isDelete;


}
