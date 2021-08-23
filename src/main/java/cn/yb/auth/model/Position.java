package cn.yb.auth.model;

import lombok.Data;

import java.util.List;

/**
 * 职位
 * 只有在职位下面才可以加人
 */
@Data
public class Position {
    private String id;
    private String projectid;
    /**
     * 分组标签
     */
    private  String grouplabel;
    /**
     * 分组关键字
     */
    private  String groupkey;
    /**
     * 职位名称
     */
    private String name;
    /**
     * 职位关键值
     */
    private String key;
    private String pid;
    private int level;
    private int seq;
}
