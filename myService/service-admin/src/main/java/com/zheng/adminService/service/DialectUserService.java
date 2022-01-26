package com.zheng.adminService.service;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-25
 */
public interface DialectUserService extends IService<DialectUser> {


    ResponseUtils deleteById(List<String> idList);
}
