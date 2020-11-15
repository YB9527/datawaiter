package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.service.impl.ISysService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import java.sql.Connection;
import java.util.List;

public class BasicController {

    @Autowired
    protected ISysService sysService;

    public  Connection SysConn;
    protected final static int CODE_SUCCESS=1000;
    protected final static int CODE_ERROR=1001;
    protected final String RESPON_MESSAGE_SAVE_SUCCESS = "保存成功";
    protected final String RESPON_MESSAGE_SAVE_ERROR = "保存失败";
    protected final String RESPON_MESSAGE_FIND_SUCCESS = "查找成功";
    protected final String RESPON_MESSAGE_DELETE_SUCCESS = "删除成功";
    protected final String RESPON_MESSAGE_DELETE_ERROR = "删除失败";
    protected final String RESPON_MESSAGE_UPDATE_SUCCESS = "修改成功";
    protected final String RESPON_MESSAGE_UPDATE_ERROR = "修改失败";
    protected final String RESPON_OK = "成功";

    @Autowired
    SystemConnect systemConnect;
    @Value("${SystemDatabase.ip}")
    public  void setIp(String ip) {
        SysConn = systemConnect.getConn();
    }

    public String  sqlStr(String str){
        return  "\""+str+"\"";
    }
    protected Respon responBasicError() {
        Respon respon = new Respon(false, CODE_ERROR, "失败", "{}");
        return respon;
    }
    protected Respon responError(String msg) {
        Respon respon = new Respon(false, CODE_ERROR, msg, "{}");
        return respon;
    }

    protected Respon responBasicSuccess() {
        Respon respon = new Respon(false, CODE_SUCCESS, "成功", "{}");
        return respon;
    }

    public Respon responOk(Object object) {

        return  respon(true,RESPON_OK,object);
    }
    public Respon respon(Boolean isOk, String message, Object object) {
        int resultcode=CODE_ERROR;
        if(isOk != null && isOk){
            resultcode = CODE_SUCCESS;
        }
        Respon respon = getRespon(isOk, resultcode, message, object);
        return respon;
    }
    protected Respon getRespon(Boolean isOk, Integer code, String message, Object object) {
        Respon respon = new Respon(isOk, code, message, object);
        return respon;
    }
}
