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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        String addressName = address.getAddressName();
        if(StringUtils.isNotEmpty(addressName)){
            wrapper.like("address_name",addressName);
        }
        Page<DialectAddress> Page = new Page<>(page,limit);
        IPage<DialectAddress> addressIPage = addressService.page(Page, wrapper);
        return ResponseUtils.ok().data("items",addressIPage.getRecords()).data("total",addressIPage.getTotal());
    }

    @Cacheable(value = "address",key = "'addressList'")
    @GetMapping("/getList")
    public ResponseUtils getList(){
        List<DialectAddress> list = addressService.getAddressList();
        return ResponseUtils.ok().data("items",list);
    }

    @CacheEvict(value = "address",allEntries = true)
    @PostMapping("/save")
    public ResponseUtils saveAddress(DialectAddress address){
        String province = address.getProvince();
        String city = address.getCity();
        String county = address.getCounty();
        //拼接名字
        String addressName=province+"-"+city+"-"+county;
        QueryWrapper<DialectAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("address_name",addressName).eq("is_delete",0);
        DialectAddress one = addressService.getOne(wrapper);
        if(one!=null){
            return ResponseUtils.error().message("添加失败！！已经存在相同的地名！！");
        }
        address.setAddressName(province+"-"+city+"-"+county);
        addressService.save(address);
        return ResponseUtils.ok();
    }

    @CacheEvict(value = "address",allEntries = true)
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
    @Cacheable(value = "address",key = "'getOne'+#id")
    @GetMapping("/getOne/{id}")
    public ResponseUtils getOne(@PathVariable("id") String id){
        DialectAddress dialectAddress = addressService.getById(id);
        return ResponseUtils.ok().data("item",dialectAddress);
    }

    @CacheEvict(value = "address",allEntries = true)
    @DeleteMapping("deleteByIds")
    public ResponseUtils deleteByIds(@RequestBody List<String> idList){
       return addressService.deleteByIds(idList);
    }
}


