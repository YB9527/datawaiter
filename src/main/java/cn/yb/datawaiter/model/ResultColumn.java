package cn.yb.datawaiter.model;

import cn.yb.datawaiter.jdbc.model.TableColumn;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResultColumn {
    private String id;
    private String property;
    private String column_;
    private PoRelation poRelation;
    private String mapperId;
    /**
     * 级联关联时，column_ 对应的 mapper
     */
    private String column_MapperId;
    /**
     * 测试值
     */
    private String testValue;

    public ResultColumn() {

    }

    public ResultColumn(String mapperId, TableColumn tc) {
        id = UUID.randomUUID().toString();
        property = "[" + tc.getColumnName() + "]";
        column_ = "";
        poRelation = PoRelation.no;
        this.mapperId = mapperId;
    }

    public ResultColumn(String mapperId, String property, String testValue) {
        id = UUID.randomUUID().toString();
        this.property = "[" + property + "]";
        column_ = "";
        poRelation = PoRelation.no;
        this.testValue = testValue;
        this.mapperId = mapperId;
    }



   /* public ResultColumn(String mapperId, TableColumn tc) {
        id = UUID.randomUUID().toString();
        property = tc.getColumnName();
        column_ = tc.getColumnName();
        poRelation = PoRelation.no;
        this.mapperId= mapperId;
    }*/
}
