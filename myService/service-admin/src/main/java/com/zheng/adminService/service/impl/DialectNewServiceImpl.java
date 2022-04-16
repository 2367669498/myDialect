package com.zheng.adminService.service.impl;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.entity.DialectNew;
import com.zheng.adminService.mapper.DialectNewMapper;
import com.zheng.adminService.service.DialectNewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-06
 */
@Service
public class DialectNewServiceImpl extends ServiceImpl<DialectNewMapper, DialectNew> implements DialectNewService {

    @Override
    public ResponseUtils deleteById(List<String> idList) {
        List<DialectNew> list = this.baseMapper.selectBatchIds(idList);
        if (list != null) {
            for (DialectNew dialectNew : list) {
                dialectNew.setIsDelete(1);
                this.baseMapper.updateById(dialectNew);
            }
            return ResponseUtils.ok();
        }
        return ResponseUtils.error().message("不存在该数据！！");
    }
}
