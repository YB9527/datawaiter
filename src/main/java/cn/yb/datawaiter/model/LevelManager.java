package cn.yb.datawaiter.model;

import lombok.Data;

/**
 * 层级管理
 */
@Data
public class LevelManager {
    private  String id;
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
    private  String describe;
    /**
     * 父节点id
     */
    private  String parentId;
}
