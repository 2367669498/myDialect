package com.zheng.adminService.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddressOneVo {

    //����
    private String name;

    List<AddressTwoVo> addressTwoList;
}
