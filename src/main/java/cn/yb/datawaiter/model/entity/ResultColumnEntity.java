package cn.yb.datawaiter.model.entity;

import cn.yb.datawaiter.jdbc.model.TableColumn;
import lombok.Data;

import javax.persistence.Table;
import java.util.UUID;

@Data
@Table(name = "resultcolumn")
public class ResultColumnEntity {
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

    public ResultColumnEntity() {

    }

    public ResultColumnEntity(String mapperId, TableColumn tc) {
        id = UUID.randomUUID().toString();
        property = "[" + tc.getColumnName() + "]";
        column_ = "";
        poRelation = PoRelation.no;
        this.mapperId = mapperId;
    }

    public ResultColumnEntity(String mapperId, String property, String testValue) {
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
