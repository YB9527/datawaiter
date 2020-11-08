package cn.yb.datawaiter.model;

import lombok.Data;

@Data
public class User {
    private  String id;
    private  String name;
    private  String password;
    /**
     * 用户权限
     */
    private  String accessId;
}
