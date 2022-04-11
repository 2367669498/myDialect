package com.zheng.adminService.controller;


import com.zheng.Utils.JwtUtils;
import com.zheng.Utils.MD5;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.entity.DialectRegional;
import com.zheng.adminService.entity.vo.DialectRegionalVo;
import com.zheng.adminService.entity.vo.MemberVo;
import com.zheng.adminService.service.DialectMemberService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/member")
@CrossOrigin
public class DialectMemberController {

    @Autowired
    private DialectMemberService dialectMemberService;

    //登录
    @PostMapping("login")
    public ResponseUtils loginUser(@RequestBody MemberVo member) {
        //member对象封装手机号和密码
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token = dialectMemberService.login(member);
        return ResponseUtils.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public ResponseUtils registerUser(@RequestBody MemberVo memberVo) {
        return dialectMemberService.register(memberVo);

    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public ResponseUtils getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        DialectMember member = dialectMemberService.getById(memberId);
        return ResponseUtils.ok().data("userInfo",member);
    }

    @GetMapping("getById/{id}")
    public ResponseUtils getById(@ApiParam(name = "id",value = "id",required = true) @PathVariable String id){
        DialectMember byId = dialectMemberService.getById(id);
        return ResponseUtils.ok().data("byId",byId);
    }

    @PostMapping("updateById")
    public ResponseUtils updateById(@RequestBody MemberVo memberVo){
        DialectMember dialectMember = new DialectMember();
        dialectMember.setId(memberVo.getId());
        dialectMember.setAvater(memberVo.getAvater());
        dialectMember.setUsername(memberVo.getUsername());
        dialectMember.setPhone(memberVo.getPhone());
        dialectMember.setPassword(MD5.encrypt(memberVo.getPassword()));
        dialectMemberService.updateById(dialectMember);
        return ResponseUtils.ok();
    }
}

