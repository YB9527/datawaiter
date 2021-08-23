package cn.yb.auth.model;

import lombok.Data;

import java.util.List;

/**
 * 组织
 */
@Data
public class Organization {
    private String id;
    private  String projectid;
    private  String name;
    private  String key;
    private  String pid;
    private  int level;
    private  int seq;

}
