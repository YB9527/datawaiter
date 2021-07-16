package cn.yb.auth.model;

import lombok.Data;

import java.util.List;

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
     * 部门
     */
    private List<Department> departments;
    /**
     * 职位
     */
    private List<Position> positions;
    /**
     * 用户状态
     */
    private String status;

}
