package com.zheng.adminService.service;

import com.zheng.Utils.ResponseUtils;
import com.zheng.adminService.entity.DialectMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.adminService.entity.vo.MemberVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhengdi
 * @since 2022-04-04
 */
public interface DialectMemberService extends IService<DialectMember> {

    String login(MemberVo member);

    ResponseUtils register(MemberVo memberVo);

    DialectMember getOpenIdMember(String openid);

    ResponseUtils deleteById(List<String> idList);
}
