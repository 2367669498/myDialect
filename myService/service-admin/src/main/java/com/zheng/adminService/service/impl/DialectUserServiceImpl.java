package com.zheng.adminService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zheng.Utils.JwtUtils;
import com.zheng.Utils.MD5;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.entity.DialectUser;
import com.zheng.adminService.entity.vo.LoginVo;
import com.zheng.adminService.mapper.DialectUserMapper;
import com.zheng.adminService.service.DialectUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhengdi
 * @since 2022-01-25
 */
@Service
public class DialectUserServiceImpl extends ServiceImpl<DialectUserMapper, DialectUser> implements DialectUserService {


    @Override
    public ResponseUtils deleteById(List<String> idList) {
        List<DialectUser> list = this.baseMapper.selectBatchIds(idList);
        if(list!=null){
            for (DialectUser dialectUser : list) {
                dialectUser.setIsDelete(1);
                this.baseMapper.updateById(dialectUser);
            }
            return ResponseUtils.ok();
        }
        return ResponseUtils.error().message("不存在该数据！！");
    }

    @Override
    public String login(LoginVo vo) {
        //获取登录手机号和密码
        String username = vo.getUsername();
        String password = vo.getPassword();

        //手机号和密码非空判断
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BaseException(20001, "登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<DialectUser> phoneWrapper = new QueryWrapper<>();
        phoneWrapper.eq("name", username).eq("is_delete", 0);
        DialectUser user = baseMapper.selectOne(phoneWrapper);
        //判断查询对象是否为空
        if (user == null) {
            throw new BaseException(20001, "登录失败");
        }

        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式 MD5

        if (!MD5.encrypt(password).equals(user.getPwd())) {
            throw new BaseException(20001, "密码错误");
        }
        if (user.getStatus() == 2) {
            throw new BaseException(20001, "用户被禁用");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(user.getId(), user.getPwd());
        return jwtToken;

    }
}
