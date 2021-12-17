package cn.yb.datawaiter.jdbc.model;

import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

  public class SelectBuild {
    private String tableName ;
    private String sql ;
    private String order ="" ;
    private  int pagenum;
    private  int pagecount;
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

    public  SelectBuild setLimit(int pagenum, int pagecount) {
        this.pagenum = pagenum;
        this.pagecount = pagecount;
        return  this;
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
        if(pagenum > 0 && pagecount >0){
            int start = (pagenum-1) * pagecount;
            int end =  pagecount;
            sql += "limit " +start +","+end;
        }
        return Select.findDataBySQL( conn,sql,tClass);
    }

    public int findCount(Connection conn) {
        String where = Select.getWhereSQL(map);
        if(!Tool.isEmpty(where)){
            where = " WHERE " + where;
        }
        sql = "SELECT count(*) as count FROM "+ tableName + where  ;
        List<JSONObject> json =  Select.findDataBySQL( conn,sql);
        if(json.size() > 0){
            return  json.get(0).getInteger("count");
        }
        return  0;
    }
}
