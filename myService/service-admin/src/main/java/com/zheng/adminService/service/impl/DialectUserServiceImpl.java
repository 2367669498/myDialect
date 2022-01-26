package com.zheng.adminService.service.impl;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.mapper.DialectUserMapper;
import com.zheng.adminService.service.DialectUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-25
 */
@Service
public class DialectUserServiceImpl extends ServiceImpl<DialectUserMapper, DialectUser> implements DialectUserService {


    @Override
    public ResponseUtils deleteById(List<String> idList) {
        List<DialectUser> list = this.baseMapper.selectBatchIds(idList);
        if(list!=null){
            for (DialectUser dialectUser : list) {
                dialectUser.setIsDelete(1);
                this.baseMapper.updateById(dialectUser);
            }
            return ResponseUtils.ok();
        }
        return ResponseUtils.error().message("不存在该数据！！");
    }
}
