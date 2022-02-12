package com.zheng.adminService.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddressOneVo {

    //Ãû³Æ
    private String name;

    List<AddressTwoVo> addressTwoList;
}
