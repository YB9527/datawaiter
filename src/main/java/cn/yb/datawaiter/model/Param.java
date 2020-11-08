package cn.yb.datawaiter.model;

import lombok.Data;

@Data
public class Param {
    private String id;
    /**
     * api 的 id 号
     */
    private String apiId;
    /**
     * 参数名字
     */
    private String paramName;

    /**
     * 测试值
     */
    private String testValue;

    public Param(){

    }
    public Param(String paramName, String value) {
    }
}
