package cn.yb.datawaiter.jdbc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLParam {
    private  String sql;
    private List<Object> params;
    public SQLParam(){
        params  = new ArrayList<>();
    }

    public SQLParam(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params;
    }

    public SQLParam appendSQL(String str) {
        if(sql == null){
            sql = str;
        }else{
            sql += " "+ str;
        }
        return  this;
    }

    public SQLParam appendSQLStart(String str) {
        if(sql == null){
            sql = str;
        }else{
            sql = str +" "+ sql;
        }
        return  this;
    }
}
