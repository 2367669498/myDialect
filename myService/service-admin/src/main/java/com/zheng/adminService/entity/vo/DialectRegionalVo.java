package com.zheng.adminService.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DialectRegionalVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "����������")
    private String regionalName;

    @ApiModelProperty(value = "�����ɼ���ʽ 1������  2���ʻ�  3������  4����ҥ")
    private Integer regionalType;

    @ApiModelProperty(value = "�Ƿ������Ƶ 0�������� 1������")
    private Integer isVideo;

    @ApiModelProperty(value = "������Ƶ�����ļ�id")
    private String videoId;

    @ApiModelProperty(value = "�Ƿ������Ƶ 0�������� 1������")
    private Integer isMusic;

    @ApiModelProperty(value = "������Ƶ�����ļ�id")
    private String musicId;

    @ApiModelProperty(value = "������id")
    private String addressId;

    @ApiModelProperty(value = "������")
    private String addressName;

    @ApiModelProperty(value = "����������˵��")
    private String addressDetailed;

    @ApiModelProperty(value = "��ϸ˵��")
    private String detailed;

    @ApiModelProperty(value = "�ϴ���id")
    private String uploadUserId;

    @ApiModelProperty(value = "״̬��1������� 2����ͨ�� 3������")
    private Integer status;
}
