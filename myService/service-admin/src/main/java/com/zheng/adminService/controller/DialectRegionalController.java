package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jmx.snmp.BerException;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.service.DialectAddressService;
import com.zheng.adminService.service.DialectRegionalService;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private DialectAddressService addressService;
    @ApiOperation(value = "分页查询列表")
    @GetMapping("/getPageList/{page}/{limit}")
    public ResponseUtils getPageList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "searchObj", value = "查询对象", required = false) DialectRegional dialectRegional){
        QueryWrapper<DialectRegional> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        wrapper.orderByAsc("status");
        if(dialectRegional!=null){
            String regionalName = dialectRegional.getRegionalName();
            String addressName = dialectRegional.getAddressName();
            Integer regionalType = dialectRegional.getRegionalType();
            if(StringUtils.isNotEmpty(regionalName)){
                wrapper.like("regional_name",regionalName);
            }
            if(StringUtils.isNotEmpty(addressName)){
                wrapper.eq("address_name",addressName);
            }
            if(regionalType!=null){
                wrapper.eq("regional_type",regionalType);
            }
        }
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
        String addressId = regional.getAddressId();
        DialectAddress address = addressService.getById(addressId);
        regional.setAddressName(address.getAddressName());
        regionalService.save(regional);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "根据id更改状态")
    @PostMapping("/changeStatus/{id}")
    public ResponseUtils changeStatus(@PathVariable("id") Long id){
        DialectRegional byId = regionalService.getById(id);
        byId.setStatus(byId.getStatus()==1?2:1);
        regionalService.updateById(byId);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/deleteRegional")
    public ResponseUtils deleteRegional(@RequestBody List<String> idList){
        return regionalService.deleteById(idList);
    }
}

