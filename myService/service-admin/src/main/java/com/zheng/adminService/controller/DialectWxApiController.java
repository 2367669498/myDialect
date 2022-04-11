package com.zheng.adminService.controller;
import com.google.gson.Gson;
import com.zheng.Utils.JwtUtils;
import com.zheng.adminService.entity.DialectMember;
import com.zheng.adminService.service.DialectMemberService;
import com.zheng.adminService.utils.ConstantWxUtils;
import com.zheng.adminService.utils.HttpClientUtils;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class DialectWxApiController {

    @Autowired
    private DialectMemberService dialectMemberService;

    //2 ��ȡɨ������Ϣ���������
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            //1 ��ȡcodeֵ����ʱƱ�ݣ���������֤��
            //2 ����code���� ΢�Ź̶��ĵ�ַ���õ�����ֵ accsess_token �� openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //ƴ���������� ��id  ��Կ �� codeֵ
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //�������ƴ�Ӻõĵ�ַ���õ���������ֵ accsess_token �� openid
            //ʹ��httpclient�������󣬵õ����ؽ��
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //��accessTokenInfo�ַ�����ȡ��������ֵ accsess_token �� openid
            //��accessTokenInfo�ַ���ת��map���ϣ�����map����key��ȡ��Ӧֵ
            //ʹ��jsonת������ Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");

            //��ɨ������Ϣ������ݿ�����
            //�ж����ݱ������Ƿ������ͬ΢����Ϣ������openid�ж�
            DialectMember member = dialectMemberService.getOpenIdMember(openid);
            if(member == null) {//memeber�ǿգ���û����ͬ΢�����ݣ��������

                //3 ���ŵõ�accsess_token �� openid����ȥ����΢���ṩ�̶��ĵ�ַ����ȡ��ɨ������Ϣ
                //����΢�ŵ���Դ����������ȡ�û���Ϣ
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //ƴ����������
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //��������
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //��ȡ����userinfo�ַ���ɨ������Ϣ
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname");//�ǳ�
                String headimgurl = (String)userInfoMap.get("headimgurl");//ͷ��

                member = new DialectMember();
                member.setOpenid(openid);
                member.setUsername(nickname);
                member.setAvater(headimgurl);
                dialectMemberService.save(member);
            }

            //ʹ��jwt����member��������token�ַ���
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getUsername());
            //��󣺷�����ҳ�棬ͨ��·������token�ַ���
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch(Exception e) {
            throw new BaseException(20001,"��¼ʧ��");
        }
    }

    //1 ����΢��ɨ���ά��
    @GetMapping("login")
    public String getWxCode() {
        //�̶���ַ������ƴ�Ӳ���
//        String url = "https://open.weixin.qq.com/" +
//                "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";

        // ΢�ſ���ƽ̨��ȨbaseUrl  %s�൱��?����ռλ��
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //��redirect_url����URLEncoder����
        String redirectUrl = "http://localhost:8160/api/ucenter/wx/callback";
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch(Exception e) {
            throw new BaseException(20001,"��¼ʧ��");
        }

        //����%s����ֵ
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "zhengdi"
        );

        //�ض�������΢�ŵ�ַ����
        return "redirect:"+url;
    }
}
