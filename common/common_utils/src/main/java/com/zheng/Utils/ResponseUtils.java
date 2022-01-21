package com.zheng.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class ResponseUtils {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    //把构造方法私有
    private ResponseUtils() {}

    //成功静态方法
    public static ResponseUtils ok() {
        ResponseUtils ResponseUtils = new ResponseUtils();
        ResponseUtils.setSuccess(true);
        ResponseUtils.setCode(ResultCode.SUCCESS);
        ResponseUtils.setMessage("成功");
        return ResponseUtils;
    }

    //失败静态方法
    public static ResponseUtils error() {
        ResponseUtils ResponseUtils = new ResponseUtils();
        ResponseUtils.setSuccess(false);
        ResponseUtils.setCode(ResultCode.ERROR);
        ResponseUtils.setMessage("失败");
        return ResponseUtils;
    }

    public ResponseUtils success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public ResponseUtils message(String message){
        this.setMessage(message);
        return this;
    }

    public ResponseUtils code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResponseUtils data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public ResponseUtils data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
