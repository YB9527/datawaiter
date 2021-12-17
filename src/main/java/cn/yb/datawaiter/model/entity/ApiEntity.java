package cn.yb.datawaiter.model.entity;

import cn.yb.datawaiter.jdbc.model.CRUDEnum;
import lombok.Data;

import java.util.Date;

@Data
public class ApiEntity {
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
     * 数据操作
     */
    private CRUDEnum crud;
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
     * 项目id
     */
    private String projectid;

    private  String mapperId;
    private MapperEntity mapperEntity;
    private Date date;

}
