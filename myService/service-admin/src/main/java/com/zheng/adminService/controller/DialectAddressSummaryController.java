package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectAddressSummary;
import com.zheng.adminService.entity.vo.AddressOneVo;
import com.zheng.adminService.entity.vo.AddressThreeVo;
import com.zheng.adminService.entity.vo.AddressTwoVo;
import com.zheng.adminService.service.DialectAddressSummaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-02-12
 */
@RestController
@RequestMapping("/address")
@CrossOrigin
public class DialectAddressSummaryController {
    @Autowired
    private DialectAddressSummaryService addressSummaryService;

    @PostMapping("saveAddressSummary")
    public ResponseUtils save(DialectAddressSummary addressSummary){
        addressSummaryService.save(addressSummary);
        return ResponseUtils.ok();
    }

    @GetMapping("getProvinceList")
    public ResponseUtils getProvinceList(){
        QueryWrapper<DialectAddressSummary> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        wrapper.eq("parent_name","");
        List<DialectAddressSummary> list = addressSummaryService.list(wrapper);
        //省级地名
        List<AddressOneVo> addressOneVos = new ArrayList<>();
        for (DialectAddressSummary dialectAddressSummary : list) {
            //市级地名
            List<AddressTwoVo> addressTwoVos = new ArrayList<>();
            AddressOneVo addressOneVo = new AddressOneVo();
            String name = dialectAddressSummary.getName();
            addressOneVo.setName(name);
            QueryWrapper<DialectAddressSummary> wrapperTwo = new QueryWrapper<>();
            wrapperTwo.eq("is_delete",0).eq("parent_name",name);
            List<DialectAddressSummary> listTwo = addressSummaryService.list(wrapperTwo);
            for (DialectAddressSummary addressSummary : listTwo) {
                //县级地名
                List<AddressThreeVo> addressThreeVos = new ArrayList<>();
                AddressTwoVo addressTwoVo = new AddressTwoVo();
                String nameTwo = addressSummary.getName();
                addressTwoVo.setName(nameTwo);
                QueryWrapper<DialectAddressSummary> wrapperThree = new QueryWrapper<>();
                wrapperThree.eq("is_delete",0).eq("parent_name",nameTwo);
                List<DialectAddressSummary> listThree = addressSummaryService.list(wrapperThree);
                for (DialectAddressSummary summary : listThree) {
                    AddressThreeVo addressThreeVo = new AddressThreeVo();
                    BeanUtils.copyProperties(summary,addressThreeVo);
                    addressThreeVos.add(addressThreeVo);
                }
                addressTwoVo.setAddressThreeList(addressThreeVos);
                addressTwoVos.add(addressTwoVo);
            }
            addressOneVo.setAddressTwoList(addressTwoVos);
            addressOneVos.add(addressOneVo);
        }
        return ResponseUtils.ok().data("list",addressOneVos);
    }
}

