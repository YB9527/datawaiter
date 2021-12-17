package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

@Data
public class FieldWhere {
    private String connectSymbol;
    private String field;
    private Object vaule;

    public FieldWhere(String connectSymbol, String field, Object vaule) {
        this.connectSymbol = connectSymbol;
        this.field = field;
        this.vaule = vaule;
    }
}
