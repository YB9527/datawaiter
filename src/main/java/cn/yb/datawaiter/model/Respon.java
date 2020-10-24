package cn.yb.datawaiter.model;

import lombok.Data;

@Data
public class Respon {

    private Boolean isOk;
    private Integer code;
    private String msg;
    private Object data;

    public Respon(){

    }
    public Respon(Boolean isOk, Integer code, String msg, Object data) {
        this.isOk = isOk;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}


