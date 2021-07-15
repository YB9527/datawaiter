package cn.yb.auth.model;

import lombok.Data;

@Data
public class User {
    private String id;
    /**
     * 昵称
     */
    private String name;
    private String phone;
    private String account;
    private String password;
    private String registdate;
    /**
     * 用户状态
     */
    private String status;
    /**
     * 部门id
     */
    private String departmentid;
    /**
     * 职位id
     */
    private String postionid;
}
