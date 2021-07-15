package cn.yb.datawaiter.model;

import cn.yb.datawaiter.controller.BasicController;
import lombok.Data;

import java.util.Date;

@Data
public class Respon {
    private final static int CODE_SUCCESS=1000;
    private final static int CODE_ERROR=1001;
    private Boolean success ;
    private Boolean isOk;
    private Integer code;
    private String msg;
    private Object data;

    private Date startDate;
    private Date endDate;
    private String  totalTime;

    public Respon(Date startDate) {
        this.startDate = startDate;
    }

    public Respon(){

    }
    public Respon(Boolean isOk, Integer code, String msg, Object data) {
        this.success = isOk;
        this.isOk = isOk;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public void  buildRespon(Boolean isOk, Integer code, String msg, Object data) {
        this.isOk = isOk;
        this.success = isOk;
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.endDate = new Date();
        this.totalTime = (endDate.getTime() - startDate.getTime())+" 毫秒";
    }

    public Respon ok(Object obj) {
        this.buildRespon(true, CODE_SUCCESS, "成功", obj);
        return this;
    }

    public Respon responBasicError() {
        Respon respon = new Respon(false, CODE_ERROR, "失败", "{}");
        return respon;
    }
    public Respon responError(String msg) {
        Respon respon = new Respon(false, CODE_ERROR, msg, "{}");
        return respon;
    }

    public Respon responBasicSuccess() {
        Respon respon = new Respon(false, CODE_SUCCESS, "成功", "{}");
        return respon;
    }

    public Respon count(int count,String error) {
        if(count > 0){
           return  ok(count);
        }else{
            return  responError(error);
        }
    }
    public Respon count(int count) {
        return  count(count,"失败");
    }
}


