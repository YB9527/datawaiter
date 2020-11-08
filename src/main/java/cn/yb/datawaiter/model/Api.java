package cn.yb.datawaiter.model;

import lombok.Data;

import java.util.List;

@Data
public class Api {
    private String id;
    /**
     * 数据库连接的id
     */
    private String databaseConnectId;
    /**
     * 访问控制的id
     */
    private String accessId;
    /**
     * 请求方式 get、post、、
     */
    private QuestMethod questMethod;
    /**
     * 访问路径的层级 api
     */
    private  String levelId;
    /**
     * 本级URL
     */
    private String selfURL;
    /**
     *  URL 根路径
     */
    private  String rootURL;
    /**
     * api 名字
     */
    private String label;
    /**
     * sql 语句
     */
    private String sql_;

    private List<Param> params;
}
