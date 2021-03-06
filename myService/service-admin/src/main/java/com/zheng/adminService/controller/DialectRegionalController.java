package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jmx.snmp.BerException;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.*;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                     @ApiParam(name = "addressId", value = "查询对象", required = true) @PathVariable String addressId,
                                     @ApiParam(name = "searchObj", value = "查询对象", required = false) DialectRegional dialectRegional){
        QueryWrapper<DialectRegional> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0).eq("regional_type",type).eq("status",2).eq("address_id",addressId);
        if(dialectRegional!=null){
            String regionalName = dialectRegional.getRegionalName();
            if(StringUtils.isNotEmpty(regionalName)){
                wrapper.like("regional_name",regionalName);
            }
        }
        Page<DialectRegional> regionalPage = new Page<>(page,limit);
        IPage<DialectRegional> regionalIPage = regionalService.page(regionalPage, wrapper);
        return ResponseUtils.ok().data("items",regionalIPage.getRecords()).data("total",regionalIPage.getTotal());
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

    @Cacheable(value = "regional",key = "'getOne'+#id")
    @ApiOperation(value = "查看单个")
    @GetMapping("/getById/{id}")
    public ResponseUtils getById(@PathVariable String id){
        DialectRegional byId = regionalService.getById(id);
        if(byId==null){
            throw new BaseException(20001,"不存在该信息");
        }
        return ResponseUtils.ok().data("item",byId);
    }
    @CacheEvict(value = "regional",allEntries = true)
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

    @CacheEvict(value = "regional",allEntries = true)
    @ApiOperation(value = "根据id更改状态")
    @PostMapping("/changeStatus/{id}")
    public ResponseUtils changeStatus(@PathVariable("id") Long id){
        DialectRegional byId = regionalService.getById(id);
        byId.setStatus(byId.getStatus()==1?2:1);
        regionalService.updateById(byId);
        return ResponseUtils.ok();
    }

    @CacheEvict(value = "regional",allEntries = true)
    @ApiOperation(value = "根据id启用禁用状态")
    @PostMapping("/changeStatus1/{id}")
    public ResponseUtils changeStatus1(@PathVariable("id") Long id){
        DialectRegional byId = regionalService.getById(id);
        byId.setStatus(byId.getStatus()==2?3:2);
        regionalService.updateById(byId);
        return ResponseUtils.ok();
    }

    @CacheEvict(value = "regional",allEntries = true)
    @ApiOperation(value = "批量删除")
    @DeleteMapping("/deleteRegional")
    public ResponseUtils deleteRegional(@RequestBody List<String> idList){
        return regionalService.deleteById(idList);
    }

    @CacheEvict(value = "regional",allEntries = true)
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
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 1).eq("is_delete",0)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 2).eq("is_delete",0)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 3).eq("is_delete",0)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 4).eq("is_delete",0)));
        dataVo.setNumber(list);
        dataVos.add(dataVo);



        DataVo dataVo1 = new DataVo();
        QueryWrapper<DialectAddress> wrapper = new QueryWrapper<>();
        wrapper.groupBy("address_name").eq("is_delete",0);
        List<DialectAddress> list1 = addressService.list(wrapper);
        List<String> list2 = new ArrayList<>();
        List<Integer> listNumber2 = new ArrayList<>();
        for (DialectAddress address : list1) {
            QueryWrapper<DialectRegional> wr= new QueryWrapper<>();
            wr.eq("address_id",address.getId());
            wr.eq("is_delete",0);
            Integer count = regionalService.count(wr);
            listNumber2.add(count);
            list2.add(address.getCounty());
        }
        dataVo1.setData(list2);
        dataVo1.setNumber(listNumber2);
        dataVos.add(dataVo1);

        DataVo dataVo2 = new DataVo();
        QueryWrapper<DialectVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.select("address_id").groupBy("address_id").eq("is_delete",0);
        List<DialectVideo> list4 = dialectVideoService.list(wrapper1);
        List<String> list3 = new ArrayList<>();
        List<Integer> listNumber3 = new ArrayList<>();
        for (DialectVideo video : list4) {
            QueryWrapper<DialectVideo> wr= new QueryWrapper<>();
            wr.eq("address_id",video.getAddressId()).eq("is_delete",0);
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

    @ApiOperation(value = "根据type分页查询列表")
    @GetMapping("/getList/{page}/{limit}/{memberId}")
    public ResponseUtils getPageList(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                                     @ApiParam(name = "memberId", value = "查询对象", required = true) @PathVariable String memberId,
                                     @ApiParam(name = "searchObj", value = "查询对象", required = false) DialectRegional dialectRegional) {
        QueryWrapper<DialectRegional> regionalWrapper = new QueryWrapper<>();
        regionalWrapper.eq("upload_user_id", memberId).eq("is_delete",0);
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

    //前台首页面数据分析
    @GetMapping("/dataList")
    public ResponseUtils dataList(){

        List<Map> mapArrayList1 = new ArrayList<>();
        List<String> addressNameList = new ArrayList<>();
        QueryWrapper<DialectAddress> wrapper = new QueryWrapper<>();
        wrapper.groupBy("address_name").eq("is_delete",0);
        List<DialectAddress> list1 = addressService.list(wrapper);
        for (DialectAddress address : list1) {
            Map<String, String> map1 = new HashMap<>();
            QueryWrapper<DialectRegional> wr= new QueryWrapper<>();
            wr.eq("address_id",address.getId());
            wr.eq("is_delete",0);
            Integer count = regionalService.count(wr);
            map1.put("value",count.toString());
            map1.put("name",address.getCounty());
            addressNameList.add(address.getCounty());
            mapArrayList1.add(map1);
        }

        List<Map> mapArrayList2 = new ArrayList<>();
        String[] type = {"单字","词汇","例句","民谣"};
        for(int i =0;i<type.length;i++){
            Map<String, String> map2 = new HashMap<>();
            Integer count = regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", i+1).eq("is_delete", 0));
            map2.put("value",count.toString());
            map2.put("name",type[i]);
            mapArrayList2.add(map2);
        }

        DataVo dataVo = new DataVo();
        List<Integer> list = new ArrayList<>();
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 1).eq("is_delete",0)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 2).eq("is_delete",0)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 3).eq("is_delete",0)));
        list.add(regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", 4).eq("is_delete",0)));
        dataVo.setNumber(list);

        return ResponseUtils.ok().data("data1",mapArrayList1).data("data2",mapArrayList2).data("data3",dataVo).data("addressNameList",addressNameList);
    }
    //详细页面数据分析
    @GetMapping("dataById/{addressId}")
    public ResponseUtils dataById(@ApiParam(name = "addressId", value = "addressId", required = true) @PathVariable String addressId){
        List<String> dataList = new ArrayList<>();
        for(int i=1;i<5;i++){
            Integer count = regionalService.count(new QueryWrapper<DialectRegional>().eq("regional_type", i).
                    eq("is_delete", 0).eq("address_id", addressId));
            dataList.add(count.toString());
        }
        return ResponseUtils.ok().data("dataList",dataList);
    }

}

