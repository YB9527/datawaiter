package cn.yb.auth.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String id;
    private  String projectid;
    /**
     * 昵称
     */
    private String name;
    private String phone;
    private String sfzhm;
    private String account;
    private String password;
    private String registdate;

    /**
     * 所在组织架构
     */
    private  List<Organization> organizations;
    /**
     * 职位
     */
    private List<Position> positions;
    /**
     * 用户状态
     */
    private String status;

}
