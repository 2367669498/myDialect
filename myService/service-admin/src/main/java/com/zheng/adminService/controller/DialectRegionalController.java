package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jmx.snmp.BerException;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.service.DialectRegionalService;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 乡音采集模块
 前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/regional")
@CrossOrigin
public class DialectRegionalController {


    @Autowired
    private DialectRegionalService regionalService;

    @ApiOperation(value = "分页查询列表")
    @GetMapping("/getPageList/{page}/{limit}")
    public ResponseUtils getPageList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "userQuery", value = "查询对象", required = false) DialectRegional dialectRegional){
        QueryWrapper<DialectRegional> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        //TODO
        Page<DialectRegional> regionalPage = new Page<>(page,limit);
        IPage<DialectRegional> regionalIPage = regionalService.page(regionalPage, wrapper);
        return ResponseUtils.ok().data("items",regionalIPage.getRecords()).data("total",regionalIPage.getTotal());
    }


    @ApiOperation(value = "查看单个")
    @GetMapping("/getById/{id}")
    public ResponseUtils getById(@PathVariable String id){
        DialectRegional byId = regionalService.getById(id);
        if(byId==null){
            throw new BaseException(20001,"不存在该信息");
        }
        return ResponseUtils.ok().data("item",byId);
    }
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public ResponseUtils saveDialectRegional(DialectRegional regional){
        return ResponseUtils.ok();
    }
}

