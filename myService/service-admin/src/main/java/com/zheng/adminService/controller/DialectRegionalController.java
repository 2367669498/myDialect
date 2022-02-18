package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jmx.snmp.BerException;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.entity.DialectVideo;
import com.zheng.adminService.entity.vo.DataVo;
import com.zheng.adminService.entity.vo.DialectRegionalVo;
import com.zheng.adminService.entity.vo.DialectVideoVo;
import com.zheng.adminService.service.DialectAddressService;
import com.zheng.adminService.service.DialectRegionalService;
import com.zheng.adminService.service.DialectVideoService;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private DialectVideoService dialectVideoService;
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

    @ApiOperation(value = "根据type分页查询列表")
    @GetMapping("/getListByType/{page}/{limit}/{type}/{addressId}")
    public ResponseUtils getPageList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "type", value = "查询对象", required = true) @PathVariable String type,
                                     @ApiParam(name = "addressId", value = "查询对象", required = true) @PathVariable String addressId){
        QueryWrapper<DialectRegional> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0).eq("regional_type",type).eq("status",2).eq("address_id",addressId);
        Page<DialectRegional> regionalPage = new Page<>(page,limit);
        IPage<DialectRegional> regionalIPage = regionalService.page(regionalPage, wrapper);return ResponseUtils.ok().data("items",regionalIPage.getRecords()).data("total",regionalIPage.getTotal());
    }

    @ApiOperation(value = "分页查询列表（已审批的）")
    @GetMapping("/getPageListBystatus/{page}/{limit}")
    public ResponseUtils getPageList1(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "searchObj", value = "查询对象", required = false) DialectRegional dialectRegional){
        QueryWrapper<DialectRegional> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0).ne("status",1);
        //TODO 时间排序
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
        regional.setAddressDetailed(address.getDetailed());
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

    @ApiOperation(value = "根据id启用禁用状态")
    @PostMapping("/changeStatus1/{id}")
    public ResponseUtils changeStatus1(@PathVariable("id") Long id){
        DialectRegional byId = regionalService.getById(id);
        byId.setStatus(byId.getStatus()==2?3:2);
        regionalService.updateById(byId);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/deleteRegional")
    public ResponseUtils deleteRegional(@RequestBody List<String> idList){
        return regionalService.deleteById(idList);
    }

    @ApiOperation(value = "更新")
    @PostMapping("updateById")
    public ResponseUtils updateById(DialectRegionalVo DialectRegionalVo){
        String addressId = DialectRegionalVo.getAddressId();
        DialectAddress address = addressService.getById(addressId);
        DialectRegionalVo.setAddressName(address.getAddressName());
        DialectRegional regional = new DialectRegional();
        BeanUtils.copyProperties(DialectRegionalVo,regional);
        regionalService.updateById(regional);
        return ResponseUtils.ok();
    }

    @GetMapping("data")
    public ResponseUtils data(){
        List<DataVo> dataVos = new ArrayList<>();
        DataVo dataVo = new DataVo();
        List<Integer> list = new ArrayList<>();
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 1)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 2)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 3)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 4)));
        dataVo.setNumber(list);
        dataVos.add(dataVo);



        DataVo dataVo1 = new DataVo();
        QueryWrapper<DialectAddress> wrapper = new QueryWrapper<>();
        wrapper.groupBy("address_name");
        List<DialectAddress> list1 = addressService.list(wrapper);
        List<String> list2 = new ArrayList<>();
        List<Integer> listNumber2 = new ArrayList<>();
        for (DialectAddress address : list1) {
            QueryWrapper<DialectAddress> wr= new QueryWrapper<>();
            wr.eq("address_name",address.getAddressName());
            Integer count = addressService.count(wr);
            listNumber2.add(count);
            list2.add(address.getCounty());
        }
        dataVo1.setData(list2);
        dataVo1.setNumber(listNumber2);
        dataVos.add(dataVo1);

        DataVo dataVo2 = new DataVo();
        QueryWrapper<DialectVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.select("address_id").groupBy("address_id");
        List<DialectVideo> list4 = dialectVideoService.list(wrapper1);
        List<String> list3 = new ArrayList<>();
        List<Integer> listNumber3 = new ArrayList<>();
        for (DialectVideo video : list4) {
            QueryWrapper<DialectVideo> wr= new QueryWrapper<>();
            wr.eq("address_id",video.getAddressId());
            Integer count = dialectVideoService.count(wr);
            listNumber3.add(count);
            DialectAddress byId = addressService.getById(video.getAddressId());
            list3.add(byId.getCounty());
        }
        dataVo2.setData(list3);
        dataVo2.setNumber(listNumber3);
        dataVos.add(dataVo2);
        return ResponseUtils.ok().data("list",dataVos);
    }

}

