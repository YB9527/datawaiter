package cn.yb.datawaiter.jdbc.model;

import cn.yb.datawaiter.jdbc.Select;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public  class SelectBuild {
    private String tableName ;
    private String sql ;
    private String order ="" ;
    private LinkedHashMap<String,Object> map = new LinkedHashMap<>();
    List<JSONObject> list;
    private  SelectBuild(String tableName){
        this.tableName = tableName;
    }
    public  static SelectBuild newInstance(String tableName){
        return new SelectBuild(tableName);
    }
    public  static<T> SelectBuild newInstance(Class<T> tClass){
        String tableName = tClass.getSimpleName().toLowerCase();
        return newInstance(tableName);
    }
    public  SelectBuild setWhereFiled(String field, Object value){
        map.put(field,value);
        return  this;
    }
    public  SelectBuild order(String field){
        order = field;
        return  this;
    }
    public List<JSONObject> build(Connection conn){
       sql =   Select.getSQL(tableName,map);
      return Select.findDataBySQL( conn,sql);
    }
    public <T> List<T> build(Connection conn,Class<T> tClass){
        sql =   Select.getSQL(tableName,map) + " "+order;
        return Select.findDataBySQL( conn,sql,tClass);
    }
}
