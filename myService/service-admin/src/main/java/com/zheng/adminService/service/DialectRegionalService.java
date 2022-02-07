package com.zheng.adminService.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectRegional;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 乡音采集模块
 服务类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-28
 */
public interface DialectRegionalService extends IService<DialectRegional> {

    ResponseUtils deleteById(List<String> idList);
}
