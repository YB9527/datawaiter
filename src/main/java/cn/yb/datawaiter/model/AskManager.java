package cn.yb.datawaiter.model;

import lombok.Data;

import java.sql.Date;

@Data
public class AskManager {
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
    private Boolean isStartUse;
    /**
     * 开始使用时间
     */
    private Date starTime;
    /**
     * 停止使用时间
     */
    private Date stopTime;
}
