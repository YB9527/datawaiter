package cn.yb.datawaiter.model.entity;

import lombok.Data;

import javax.persistence.Table;

/**
 * 层级管理
 */
@Data
@Table(name = "level")
public class LevelEntity {
    public static String LEVEL_TYPENAME_API = "项目树";
    public static String LEVEL_TYPENAME_BEAN = "Bean配置树";
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
