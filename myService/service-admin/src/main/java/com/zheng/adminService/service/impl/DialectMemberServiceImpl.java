package com.zheng.adminService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zheng.Utils.JwtUtils;
import com.zheng.Utils.MD5;
import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.entity.vo.MemberVo;
import com.zheng.adminService.mapper.DialectMemberMapper;
import com.zheng.adminService.service.DialectMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-04
 */
@Service
public class DialectMemberServiceImpl extends ServiceImpl<DialectMemberMapper, DialectMember> implements DialectMemberService {

    @Override
    public String login(MemberVo member) {
        //获取登录手机号和密码
        String input = member.getPhone();
        String password = member.getPassword();

        //手机号和密码非空判断
        if (StringUtils.isEmpty(input) || StringUtils.isEmpty(password)) {
            throw new BaseException(20001, "登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<DialectMember> phoneWrapper = new QueryWrapper<>();
        phoneWrapper.eq("phone", input).eq("is_delete", 0);
        DialectMember phoneMember = baseMapper.selectOne(phoneWrapper);
        //判断查询对象是否为空
        if (phoneMember == null) {//没有这个手机号
            throw new BaseException(20001, "登录失败");
        }

        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式 MD5

        if (!MD5.encrypt(password).equals(phoneMember.getPassword())) {
            throw new BaseException(20001, "密码错误");
        }

        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(phoneMember.getId(), phoneMember.getUsername());
        return jwtToken;


    }

    @Override
    public ResponseUtils register(MemberVo memberVo) {
        //获取注册的数据
        String phone = memberVo.getPhone(); //手机号
        String username = memberVo.getUsername(); //昵称
        String password = memberVo.getPassword(); //密码

        //非空判断
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(username)) {
            throw new BaseException(20001, "注册失败");
        }

        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<DialectMember> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            return ResponseUtils.error().code(20002).message("手机号重复");
        }

        //数据添加数据库中
        DialectMember member = new DialectMember();
        member.setPhone(phone);
        member.setUsername(username);
        member.setPassword(MD5.encrypt(password));//密码需要加密的
        member.setAvater("https://zhengdi-1010.oss-cn-guangzhou.aliyuncs.com/1.jpg");
        baseMapper.insert(member);
        return ResponseUtils.ok();
    }


    @Override
    public DialectMember getOpenIdMember(String openid) {
        QueryWrapper<DialectMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        DialectMember member = baseMapper.selectOne(wrapper);
        return member;
    }
}
