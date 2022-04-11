package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddressSummary;
import com.zheng.adminService.entity.DialectFavorites;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.service.DialectFavoritesService;
import com.zheng.adminService.service.DialectRegionalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-06
 */
@RestController
@RequestMapping("/favorites")
@CrossOrigin
public class DialectFavoritesController {

    @Autowired
    private DialectFavoritesService favoritesService;

    @Autowired
    private DialectRegionalService regionalService;

    @PostMapping("save")
    public ResponseUtils save(@RequestBody DialectFavorites dialectFavorites) {
        String memberId = dialectFavorites.getMemberId();
        String regionalId = dialectFavorites.getRegionalId();
        QueryWrapper<DialectFavorites> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("regional_id", regionalId);
        DialectFavorites one = favoritesService.getOne(wrapper);
        if (one != null) {
            return ResponseUtils.error().code(20002).message("已收藏过");
        }
        favoritesService.save(dialectFavorites);
        return ResponseUtils.ok();
    }

    @DeleteMapping("delete")
    public ResponseUtils deleteByIds(@RequestBody DialectFavorites dialectFavorites) {
        String memberId = dialectFavorites.getMemberId();
        String regionalId = dialectFavorites.getRegionalId();
        QueryWrapper<DialectFavorites> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("regional_id", regionalId);
        List<DialectFavorites> list = favoritesService.list(wrapper);
        List<String> idList = new ArrayList<>();
        for (DialectFavorites favorites : list) {
            idList.add(favorites.getId());
        }
        favoritesService.removeByIds(idList);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "根据type分页查询列表")
    @GetMapping("/getList/{page}/{limit}/{memberId}")
    public ResponseUtils getPageList(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                                     @ApiParam(name = "memberId", value = "查询对象", required = true) @PathVariable String memberId,
                                     @ApiParam(name = "searchObj", value = "查询对象", required = false) DialectRegional dialectRegional) {
        QueryWrapper<DialectFavorites> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        //获取收藏资源
        List<DialectFavorites> list = favoritesService.list(wrapper);
        List<String> regionalIds = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return ResponseUtils.ok();
        }
        for (DialectFavorites dialectFavorites : list) {
            String regionalId = dialectFavorites.getRegionalId();
            regionalIds.add(regionalId);
        }
        QueryWrapper<DialectRegional> regionalWrapper = new QueryWrapper<>();
        regionalWrapper.in("id", regionalIds);
        if (dialectRegional != null) {
            String regionalName = dialectRegional.getRegionalName();
            String addressName = dialectRegional.getAddressName();
            Integer regionalType = dialectRegional.getRegionalType();
            if (StringUtils.isNotEmpty(regionalName)) {
                regionalWrapper.like("regional_name", regionalName);
            }
            if (StringUtils.isNotEmpty(addressName)) {
                regionalWrapper.eq("address_name", addressName);
            }
            if (regionalType != null) {
                regionalWrapper.eq("regional_type", regionalType);
            }
        }
        Page<DialectRegional> regionalPage = new Page<>(page, limit);
        IPage<DialectRegional> regionalIPage = regionalService.page(regionalPage, regionalWrapper);
        return ResponseUtils.ok().data("items", regionalIPage.getRecords()).data("total", regionalIPage.getTotal());
    }
}

