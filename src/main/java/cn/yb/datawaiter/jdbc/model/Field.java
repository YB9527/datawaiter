package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

/**
 * 字典
 */
@Data
public class Field {
    private String id;
    private String projectid;
    private String databaseid;
    private String tablename;
    private String fieldname;
    private String fielddicid;

    private FieldDic fielddic;
}
