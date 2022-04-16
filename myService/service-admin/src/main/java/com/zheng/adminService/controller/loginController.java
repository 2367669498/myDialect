package com.zheng.adminService.controller;


import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.vo.LoginVo;
import com.zheng.adminService.service.DialectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class loginController {
    @Autowired
    private DialectUserService userService;

    @PostMapping("/login")
    public ResponseUtils login(@RequestBody LoginVo vo) {
        String token = userService.login(vo);
        return ResponseUtils.ok().data("token", token);
    }

    @GetMapping("/info")
    public ResponseUtils info() {
        return ResponseUtils.ok().data("roles", "admin");
    }

    @PostMapping("/logout")
    public ResponseUtils logout() {
        return ResponseUtils.ok();
    }
}
