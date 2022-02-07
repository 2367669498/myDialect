package com.zheng.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.baomidou.mybatisplus.extension.api.R;
import com.zheng.Utils.ResponseUtils;
import com.zheng.servicebase.ExceptionHandler.BaseException;
import com.zheng.vod.Utils.ConstantVodUtils;
import com.zheng.vod.Utils.InitVodCilent;
import com.zheng.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("uploadAlyiVideo")
    public ResponseUtils uploadAlyiVideo(MultipartFile file) {
        //返回上传视频id
        String videoId = vodService.uploadVideoAly(file);
        return ResponseUtils.ok().data("videoId",videoId);
    }

    //根据视频id删除阿里云视频
    @DeleteMapping("removeAlyVideo/{id}")
    public ResponseUtils removeAlyVideo(@PathVariable String id) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
            return ResponseUtils.ok();
        }catch(Exception e) {
            e.printStackTrace();
            throw new BaseException(20001,"删除视频失败");
        }
    }

    //删除多个阿里云视频的方法
    //参数多个视频id  List videoIdList
    @DeleteMapping("delete-batch")
    public ResponseUtils deleteBatch(@RequestParam("videoIdList") List<String> videoIdList) {
        vodService.removeMoreAlyVideo(videoIdList);
        return ResponseUtils.ok();
    }

    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public ResponseUtils getPlayAuth(@PathVariable String id) {
        try {
            //创建初始化对象
            DefaultAcsClient client =
                    InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(id);
            //调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            System.out.println(playAuth);
            return ResponseUtils.ok().data("playAuth",playAuth);
        }catch(Exception e) {
            throw new BaseException(20001,"获取凭证失败");
        }
    }

    @GetMapping("getPlayUrl/{id}")
    public ResponseUtils getPlayUrl(@PathVariable String id) {
        try {
            //创建初始化对象
            DefaultAcsClient client =
                    InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证request和response对象
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(id);
            //调用方法得到凭证
            GetPlayInfoResponse response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            String url="";
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                url=playInfo.getPlayURL();
            }
            return ResponseUtils.ok().data("url",url);
        }catch(Exception e) {
            throw new BaseException(20001,"获取凭证失败");
        }
    }
}
