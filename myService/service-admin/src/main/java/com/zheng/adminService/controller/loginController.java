package com.zheng.adminService.controller;


import com.zheng.Utils.ResponseUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class loginController {
    @PostMapping("/login")
    public ResponseUtils login(){
        return ResponseUtils.ok().data("token","admin");
    }

    @GetMapping("/info")
    public ResponseUtils info(){
        return ResponseUtils.ok().data("roles","admin").data("avatar","sssss");
    }


}
