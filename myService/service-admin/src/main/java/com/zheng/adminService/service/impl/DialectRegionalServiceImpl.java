package com.zheng.adminService.service.impl;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.mapper.DialectRegionalMapper;
import com.zheng.adminService.service.DialectRegionalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 乡音采集模块
 服务实现类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-28
 */
@Service
public class DialectRegionalServiceImpl extends ServiceImpl<DialectRegionalMapper, DialectRegional> implements DialectRegionalService {

    @Override
    public ResponseUtils deleteById(List<String> idList) {
        List<DialectRegional> list = this.baseMapper.selectBatchIds(idList);
        if(list!=null){
            for (DialectRegional regional : list) {
                regional.setIsDelete(1);
                this.baseMapper.updateById(regional);
            }
            return ResponseUtils.ok();
        }
        return ResponseUtils.error().message("不存在该数据！！");
    }
}
