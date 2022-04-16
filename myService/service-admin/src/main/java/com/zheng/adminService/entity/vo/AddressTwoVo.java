package com.zheng.adminService.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddressTwoVo {
    //名称
    private String name;

    List<AddressThreeVo> addressThreeList;
}
