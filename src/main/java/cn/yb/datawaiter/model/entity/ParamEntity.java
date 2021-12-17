package cn.yb.datawaiter.model.entity;

import lombok.Data;

@Data
public class ParamEntity {
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

    public ParamEntity(){

    }

    public ParamEntity(String paramName, String testValue) {
        this.paramName = paramName;
        this.testValue = testValue;
    }


}
