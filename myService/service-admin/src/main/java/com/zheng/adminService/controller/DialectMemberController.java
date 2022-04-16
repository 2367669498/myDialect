package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.Utils.JwtUtils;
import com.zheng.Utils.MD5;
import com.zheng.Utils.ResponseUtils;

import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.entity.vo.MemberVo;
import com.zheng.adminService.service.DialectMemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Cacheable(value = "member",key = "'getOne'+#id")
    @GetMapping("getById/{id}")
    public ResponseUtils getById(@ApiParam(name = "id",value = "id",required = true) @PathVariable String id){
        DialectMember byId = dialectMemberService.getById(id);
        String password = byId.getPassword();
        byId.setPassword(MD5.encrypt(password));
        return ResponseUtils.ok().data("byId",byId);
    }

    @CacheEvict(value = "member",allEntries = true)
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

    @ApiOperation(value = "分页用户列表")
    @GetMapping("/pageList/{page}/{limit}")
    public ResponseUtils getUserList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "userQuery", value = "查询对象", required = false) DialectMember Vo){
        Page<DialectMember> pageParam = new Page<>(page, limit);
        QueryWrapper<DialectMember> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        if(StringUtils.isNotEmpty(Vo.getUsername())){
            wrapper.like("username",Vo.getUsername());
        }
        IPage<DialectMember> dialectMemberIPage = dialectMemberService.page(pageParam, wrapper);
        Long total = dialectMemberIPage.getTotal();
        List<DialectMember> list = dialectMemberIPage.getRecords();
        return ResponseUtils.ok().data("list",list).data("total",total);
    }

    @CacheEvict(value = "member",allEntries = true)
    @ApiOperation(value = "根据id更改状态")
    @PostMapping("/changeStatus/{id}")
    public ResponseUtils changeStatus(@PathVariable("id") Long id){
        DialectMember byId = dialectMemberService.getById(id);
        byId.setStatus(byId.getStatus()==1?0:1);
        dialectMemberService.updateById(byId);
        return ResponseUtils.ok();
    }

    @CacheEvict(value = "member",allEntries = true)
    @ApiOperation(value = "批量删除")
    @DeleteMapping("/deleteUser")
    public ResponseUtils deleteUser(@RequestBody List<String> idList){
//        return ResponseUtils.error().message("yichang=============");
        return dialectMemberService.deleteById(idList);
    }

}

