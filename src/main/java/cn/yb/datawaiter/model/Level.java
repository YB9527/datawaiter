package cn.yb.datawaiter.model;

import lombok.Data;

/**
 * 层级管理
 */
@Data
public class Level {
    public static String API_LEVEL_TYPENAME = "项目树";
    private  String id;

    /**
     * 名字
     */
    private  String name;
    /**
     * 简单名字
     */
    private  String simpleName;
    /**
     * 全名称
     */
    private  String fullName;
    /**
     * 描述
     */
    private  String des;
    /**
     * 父节点id
     */
    private  String parentId;

    /**
     * 层级类型名字
     */
    private String typeName;
}
