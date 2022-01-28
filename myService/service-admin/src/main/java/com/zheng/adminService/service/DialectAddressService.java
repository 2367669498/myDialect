package com.zheng.adminService.service;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-28
 */
public interface DialectAddressService extends IService<DialectAddress> {

    ResponseUtils deleteByIds(List<String> idList);
}
