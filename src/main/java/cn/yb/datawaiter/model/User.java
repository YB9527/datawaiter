package cn.yb.datawaiter.model;

import lombok.Data;

@Data
public class User {
    private  String id;
    /**
     * 昵称
     */
    private  String name;
    /**
     * 账号
     */
    private  String account;
    private  String password;
    /**
     * 用户权限
     */
    private  String accessId;
}
