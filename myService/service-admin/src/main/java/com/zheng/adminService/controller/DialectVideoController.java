package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.Utils.MD5;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.entity.DialectVideo;
import com.zheng.adminService.entity.vo.DialectVideoVo;
import com.zheng.adminService.entity.vo.UserVo;
import com.zheng.adminService.service.DialectVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 趣味视频表 前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-02-10
 */
@RestController
@RequestMapping("/dialectVideo")
@CrossOrigin
public class DialectVideoController {
    @Autowired
    private DialectVideoService dialectVideoService;

    @ApiOperation(value = "分页查询列表")
    @GetMapping("/getPageList/{page}/{limit}")
    public ResponseUtils getPageList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "searchObj", value = "查询对象", required = false) DialectVideo dialectVideo){
        QueryWrapper<DialectVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        if(dialectVideo!=null){
            String name = dialectVideo.getName();
            String addressId = dialectVideo.getAddressId();
            if(StringUtils.isNotEmpty(name)){
                wrapper.like("name",name);
            }
            if(StringUtils.isNotEmpty(addressId)){
                wrapper.eq("address_id",addressId);
            }
        }
        Page<DialectVideo> dialectVideoPage = new Page<>(page,limit);
        IPage<DialectVideo> dialectVideoIPage = dialectVideoService.page(dialectVideoPage, wrapper);
        return ResponseUtils.ok().data("items",dialectVideoIPage.getRecords()).data("total",dialectVideoIPage.getTotal());
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public ResponseUtils saveDialectVideo(DialectVideo dialectVideo){
        dialectVideoService.save(dialectVideo);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "删除")
    @PostMapping("deleteVideo/{id}")
    public ResponseUtils deleteVideo(@PathVariable("id") String id){
        DialectVideo byId = dialectVideoService.getById(id);
        byId.setIsDelete(1);
        dialectVideoService.updateById(byId);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "查看单个")
    @GetMapping("getInfo/{id}")
    public ResponseUtils getInfo(@PathVariable("id") String id){
        DialectVideo byId = dialectVideoService.getById(id);
        return ResponseUtils.ok().data("item",byId);
    }

    @ApiOperation(value = "更新")
    @PostMapping("updateById")
    public ResponseUtils updateById(DialectVideoVo dialectVideoVo){
        DialectVideo dialectVideo = new DialectVideo();
        BeanUtils.copyProperties(dialectVideoVo,dialectVideo);
        dialectVideoService.updateById(dialectVideo);
        return ResponseUtils.ok();
    }
}

