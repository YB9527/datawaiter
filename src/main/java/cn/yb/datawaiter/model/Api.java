package cn.yb.datawaiter.model;

import lombok.Data;

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
    private String askManagerId;
    /**
     * 请求类型 get、post、、
     */
    private QuestMethod questMethod;
    /**
     * 访问路径的层级 api
     */
    private  String levelManagerId;
}
