package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.Utils.MD5;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.entity.vo.UserVo;
import com.zheng.adminService.service.DialectUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-25
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class DialectUserController {

    @Autowired
    private DialectUserService dialectUserService;

    @ApiOperation(value = "分页用户列表")
    @GetMapping("/pageList/{page}/{limit}")
    public ResponseUtils getUserList(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                                     @ApiParam(name = "userQuery", value = "查询对象", required = false) DialectUser DialectUserVo){
        Page<DialectUser> pageParam = new Page<>(page, limit);
        QueryWrapper<DialectUser> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0);
        if(StringUtils.isNotEmpty(DialectUserVo.getName())){
            wrapper.like("name",DialectUserVo.getName());
        }
        IPage<DialectUser> dialectUserIPage = dialectUserService.page(pageParam, wrapper);
        Long total = dialectUserIPage.getTotal();
        List<DialectUser> list = dialectUserIPage.getRecords();
        return ResponseUtils.ok().data("list",list).data("total",total);
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/deleteUser")
    public ResponseUtils deleteUser(@RequestBody List<String> idList){
        return dialectUserService.deleteById(idList);
    }

    @ApiOperation(value = "根据id更改状态")
    @PostMapping("/changeStatus/{id}")
    public ResponseUtils changeStatus(@PathVariable("id") Long id){
        DialectUser byId = dialectUserService.getById(id);
        byId.setStatus(byId.getStatus()==1?2:1);
        dialectUserService.updateById(byId);
        return ResponseUtils.ok();
    }

    @ApiOperation(value = "获取单个用户")
    @GetMapping("getUser/{id}")
    public ResponseUtils getUser(@PathVariable("id") Long id){
        DialectUser byId = dialectUserService.getById(id);
        return ResponseUtils.ok().data("item",byId);
    }

    @ApiOperation(value = "新增")
    @PostMapping("insertUser")
    public ResponseUtils insertUser(DialectUser user){
        if(user!=null){
            String encrypt = MD5.encrypt(user.getPwd());
            user.setPwd(encrypt);
            dialectUserService.save(user);
        }
        return ResponseUtils.ok();
    }
    @ApiOperation(value = "更新")
    @PostMapping("updateById")
    public ResponseUtils updateById(UserVo user){
        DialectUser dialectUser = new DialectUser();
        BeanUtils.copyProperties(user,dialectUser);
        //MD5加密
        String encrypt = MD5.encrypt(dialectUser.getPwd());
        dialectUser.setPwd(encrypt);
        dialectUserService.updateById(dialectUser);
        return ResponseUtils.ok();
    }

}

