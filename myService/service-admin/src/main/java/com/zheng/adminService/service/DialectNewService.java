package com.zheng.adminService.service;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectNew;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-06
 */
public interface DialectNewService extends IService<DialectNew> {

    ResponseUtils deleteById(List<String> idList);
}
