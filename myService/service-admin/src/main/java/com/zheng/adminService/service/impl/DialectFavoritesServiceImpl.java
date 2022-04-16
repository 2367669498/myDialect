package com.zheng.adminService.service.impl;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectFavorites;
import com.zheng.adminService.mapper.DialectFavoritesMapper;
import com.zheng.adminService.service.DialectFavoritesService;
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
public class DialectFavoritesServiceImpl extends ServiceImpl<DialectFavoritesMapper, DialectFavorites> implements DialectFavoritesService {

    @Override
    public ResponseUtils deleteByIds(List<String> idList) {
        List<DialectFavorites> list = this.baseMapper.selectBatchIds(idList);
        if(list!=null){
            this.baseMapper.deleteBatchIds(list);
            return ResponseUtils.ok();
        }
        return ResponseUtils.error().code(20002).message("不存在该数据！！");
    }
}
