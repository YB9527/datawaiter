package cn.yb.datawaiter.model.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Access {
    private String id;
    /**
     * 是否需要token访问
     */
    private Boolean isNeedToken;
    /**
     * 访问等级
     */
    private Integer askLevel;
    /**
     * 是否开始使用
     */
    private AccessState accessState;
    /**
     * 开始使用时间
     */
    private Date startTime;
    /**
     * 停止使用时间
     */
    private Date stopTime;
}
