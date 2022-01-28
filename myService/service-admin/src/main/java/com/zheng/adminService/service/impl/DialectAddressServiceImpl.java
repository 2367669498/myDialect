package com.zheng.adminService.service.impl;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.mapper.DialectAddressMapper;
import com.zheng.adminService.service.DialectAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-28
 */
@Service
public class DialectAddressServiceImpl extends ServiceImpl<DialectAddressMapper, DialectAddress> implements DialectAddressService {

    @Override
    public ResponseUtils deleteByIds(List<String> idList) {
        List<DialectAddress> list = this.baseMapper.selectBatchIds(idList);
        if(list!=null){
            for (DialectAddress address : list) {
                address.setIsDelete(1);
                this.baseMapper.updateById(address);
            }
            return ResponseUtils.ok();
        }
        return ResponseUtils.error().message("不存在该数据！！");
    }
}
