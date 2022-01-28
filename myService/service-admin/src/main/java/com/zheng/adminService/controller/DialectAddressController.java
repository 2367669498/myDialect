package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.vo.AddressVo;
import com.zheng.adminService.service.DialectAddressService;
import io.swagger.annotations.ApiParam;
import jdk.internal.util.xml.impl.ReaderUTF8;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/address")
@CrossOrigin
public class DialectAddressController {

    @Autowired
    private DialectAddressService addressService;

    @GetMapping("/getPageList/{page}/{limit}")
    public ResponseUtils getPageList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "Vo", value = "查询对象", required = false) DialectAddress address){
        QueryWrapper<DialectAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        //TODO
        Page<DialectAddress> Page = new Page<>(page,limit);
        IPage<DialectAddress> addressIPage = addressService.page(Page, wrapper);
        return ResponseUtils.ok().data("items",addressIPage.getRecords()).data("total",addressIPage.getTotal());
    }

    @GetMapping("/getList")
    public ResponseUtils getList(){
        //查询所有的地址,并返回地名
        QueryWrapper<DialectAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        List<DialectAddress> list = addressService.list(wrapper);
        return ResponseUtils.ok().data("items",list);
    }

    @PostMapping("/save")
    public ResponseUtils saveAddress(DialectAddress address){
        String province = address.getProvince();
        String city = address.getCity();
        String county = address.getCounty();
        //拼接名字
        address.setAddressName(province+"-"+city+"-"+county);
        addressService.save(address);
        return ResponseUtils.ok();
    }

    @PostMapping("/updateAddress")
    public ResponseUtils updateAddress(AddressVo addressVo){
        String province = addressVo.getProvince();
        String city = addressVo.getCity();
        String county = addressVo.getCounty();
        //拼接名字
        addressVo.setAddressName(province+"-"+city+"-"+county);
        DialectAddress dialectAddress = new DialectAddress();
        BeanUtils.copyProperties(addressVo,dialectAddress);
        boolean flag = addressService.updateById(dialectAddress);
        if(flag){
            return ResponseUtils.ok();
        }else {
            return ResponseUtils.error();
        }

    }

    @GetMapping("/getOne/{id}")
    public ResponseUtils getOne(@PathVariable("id") String id){
        DialectAddress dialectAddress = addressService.getById(id);
        return ResponseUtils.ok().data("item",dialectAddress);
    }

    @DeleteMapping("deleteByIds")
    public ResponseUtils deleteByIds(@RequestBody List<String> idList){
       return addressService.deleteByIds(idList);
    }
}


