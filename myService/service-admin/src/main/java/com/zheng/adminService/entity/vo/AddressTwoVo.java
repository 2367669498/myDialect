package com.zheng.adminService.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddressTwoVo {
    //����
    private String name;

    List<AddressThreeVo> addressThreeList;
}
