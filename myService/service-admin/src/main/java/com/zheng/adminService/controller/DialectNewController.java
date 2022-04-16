package com.zheng.adminService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectAddress;
import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.entity.DialectNew;
import com.zheng.adminService.entity.vo.NewVo;
import com.zheng.adminService.service.DialectAddressService;
import com.zheng.adminService.service.DialectNewService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-06
 */
@RestController
@RequestMapping("/new")
@CrossOrigin
public class DialectNewController {

    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO"; //秘钥
    @Autowired
    private DialectNewService newService;

    @Autowired
    private DialectAddressService dialectAddressService;

    @GetMapping("/pageList/{page}/{limit}")
    public ResponseUtils getList(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                                 @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                                 @ApiParam(name = "userQuery", value = "查询对象", required = false) NewVo Vo) {
        Page<DialectNew> pageParam = new Page<>(page, limit);
        QueryWrapper<DialectNew> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        if (StringUtils.isNotEmpty(Vo.getNewName())) {
            wrapper.like("new_name", Vo.getNewName());
        }
        if (StringUtils.isNotEmpty(Vo.getRegionalId())) {
            wrapper.like("regional_id", Vo.getRegionalId());
        }
        IPage<DialectNew> newIPage = newService.page(pageParam, wrapper);
        Map<String, String> map = new HashMap<>();
        Long total = newIPage.getTotal();
        List<DialectNew> list = newIPage.getRecords();
        if(list!=null && list.size()!=0){
            for (DialectNew dialectNew : list) {
                String regionalId = dialectNew.getRegionalId();
                DialectAddress byId = dialectAddressService.getById(regionalId);
                map.put(regionalId,byId.getAddressName());
            }
        }
        return ResponseUtils.ok().data("list", list).data("total", total).data("addressName",map);
    }

    @CacheEvict(value = "new",allEntries = true)
    @PostMapping("save")
    public ResponseUtils save(@RequestBody NewVo vo){
        DialectNew dialectNew = new DialectNew();
        BeanUtils.copyProperties(vo,dialectNew);
        String token = vo.getToken();
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String id = (String)claims.get("id");
        dialectNew.setUserId(id);
        newService.save(dialectNew);
        return ResponseUtils.ok();
    }

    @CacheEvict(value = "new",allEntries = true)
    @PostMapping("update")
    public ResponseUtils update(@RequestBody NewVo vo){
        DialectNew dialectNew = new DialectNew();
        BeanUtils.copyProperties(vo,dialectNew);
        String token = vo.getToken();
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String id = (String)claims.get("id");
        dialectNew.setUserId(id);
        newService.updateById(dialectNew);
        return ResponseUtils.ok();
    }

    @Cacheable(value = "new",key = "'getOne'+#id")
    @GetMapping("getOne/{id}")
    public ResponseUtils getById(@ApiParam(name = "id",value = "id",required = true) @PathVariable String id){
        DialectNew byId = newService.getById(id);
        return ResponseUtils.ok().data("byId",byId);
    }

    @CacheEvict(value = "new",allEntries = true)
    @DeleteMapping("/delete")
    public ResponseUtils deleteUser(@RequestBody List<String> idList){
        return newService.deleteById(idList);
    }

    @Cacheable(value = "new",key = "'getByAddressId'+#id")
    @GetMapping("getById/{id}")
    public ResponseUtils getByAddressId(@ApiParam(name = "id",value = "id",required = true) @PathVariable String id){
        QueryWrapper<DialectNew> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0).eq("regional_id",id);
        List<DialectNew> list = newService.list(wrapper);
        return ResponseUtils.ok().data("list",list);
    }
}

