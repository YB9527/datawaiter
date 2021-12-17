package cn.yb.datawaiter.jdbc.model;

import cn.yb.datawaiter.tools.Tool;


import java.util.List;

public class SelectBuilder extends  SQLBuilder{

    private String sql ;
    private String order ="" ;
    private  int pagenum;
    private  int pagecount;


    private SelectBuilder(String tableName) {
        super(tableName, BuilderEnum.SELECT);
    }

    public    static SelectBuilder newInstance(String tableName ){
        return new SelectBuilder(tableName);
    }

    public   static<T> SelectBuilder newInstance(Class<T> tClass ){
        String tableName = tClass.getSimpleName().toLowerCase();
        return new SelectBuilder(tableName);
    }

    public  SelectBuilder setLimit(int pagenum, int pagecount) {
        this.pagenum = pagenum;
        this.pagecount = pagecount;
        return  this;
    }

    public  SelectBuilder order(String field){
        if(Tool.isEmpty(field)){
            return this;
        }
        order = " ORDER BY " + field;
        return  this;
    }
    public SelectBuilder order(String order, String desc) {
        if(Tool.isEmpty(order)){
            return this;
        }
        return  order(order+" " + desc);
    }

    @Override
    public SelectBuilder setWhereFiled(String field, Object value) {
        return super.setWhereFiled(field, value);
    }

    @Override
    public SelectBuilder setWhereFiled(FieldWhere fieldWhere) {
        return super.setWhereFiled(fieldWhere);
    }

    @Override
    public SelectBuilder setWhereFiled_LikeOr(String value, String... searckey) {
        return super.setWhereFiled_LikeOr(value, searckey);
    }

    @Override
    public SelectBuilder  setWhereFiled_LikeOr(String smybol, String field, List values) {
        return super.setWhereFiled_LikeOr(smybol, field, values);
    }

    @Override
    public SelectBuilder setWhereMultFiled_EqOr(String symbol, String filed, List values) {
        return super.setWhereMultFiled_EqOr(symbol, filed, values);
    }

    @Override
    public SelectBuilder setWhereFiledNotNull(String symbol, String filed, Object obj) {
        return super.setWhereFiledNotNull(symbol, filed, obj);
    }

    public static String limtSQL(int pagenum, int pagecount){
        if(pagenum > 0 && pagecount >0){
            int start = (pagenum-1) * pagecount;
            return  " limit  "+pagecount+" offset "+start+" ";
        }
        return  "";
    }

    public  String findPageDataSQL(){

        sql =   Select.getPOSTSQL(tableName,fieldWheres) + " "+order;
        sql += limtSQL(pagenum,pagecount);
        return sql;
    }

    public String findCountSQL() {
        String where = Select.getWhereSQL(fieldWheres);
        if(Tool.isEmpty(where)){
            where = Select.getWhereSQL(fieldWheres);
        }
        if(!Tool.isEmpty(where)){
            where = " WHERE " + where;
        }
        sql = "SELECT count(*) as count FROM "+ tableName + where  ;
        return  sql;
    }



}
