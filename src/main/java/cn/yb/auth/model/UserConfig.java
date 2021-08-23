package cn.yb.auth.model;

import lombok.Data;

/**
 * 用户配置
 */
@Data
public class UserConfig {
    private String id;
    private String projectid;
    /**
     * 是对外开放够注册
     */
    private int sfregist;
    /**
     * 默认职位
     */
    private String deflutpostionid;
    /**
     * 默认组织
     */
    private  String deflutorganizaionid;
}
