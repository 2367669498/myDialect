package com.zheng.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/test")
    public String test(){
        return "cfghj搜索k.";
    }
    @RequestMapping("/say")
    public String say(){
        return "hello defhgfgjvtools!";
    }
}
