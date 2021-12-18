package cn.yb.datawaiter.jdbc.model;

import cn.yb.datawaiter.model.entity.ResultColumnCUD;
import cn.yb.datawaiter.tools.AnnotationTool;
import cn.yb.datawaiter.tools.Tool;


import java.util.List;
import java.util.Map;

public class Select {


    public static<T> String getSQL(Class<T> tClass, Map<String, Object> map) {
        String tableName = AnnotationTool.getTableName(tClass);
        return  getSQL(tableName,  map);
    }
    public static<T> String getSQL(String tablename, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM `" +tablename+"`");
        String where = getWhereSQL(map);
        if(!Tool.isEmpty(where)){
            sb.append(" WHERE " );
            sb.append(where);
        }
        return  sb.toString();
    }

    public static<T> String getPOSTSQL(String tablename, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM " +tablename+" ");
        String where = getWhereSQL(map);
        if(!Tool.isEmpty(where)){
            sb.append(" WHERE " );
            sb.append(where);
        }
        return  sb.toString();
    }

    public static<T> String getPOSTSQL(String tablename, List<FieldWhere> fieldWheres) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM " +tablename+" ");
        String where = getWhereSQL(fieldWheres);
        if(!Tool.isEmpty(where)){
            sb.append(" WHERE " );
            sb.append(where);
        }
        return  sb.toString();
    }

    public static String getWhereSQL(List<FieldWhere> fieldWheres) {
        StringBuilder sb = new StringBuilder();
        if(fieldWheres != null && fieldWheres.size() > 0){
            fieldWheres.forEach(item->{

                Object value = item.getVaule();
                String key = item.getField();
                if(value != null && item.getConnectSymbol() != null){
                    sb.append(item.getConnectSymbol());
                }
                if(value instanceof  String){
                    sb.append(" "+ key + "'"+value+"'");
                }else if(value != null){
                    sb.append(" "+key + value);
                }else{
                    sb.append(" "+key );
                }

            });
        }
        return  sb.toString();
    }

    public static String getWhereSQL(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        if(map != null && map.keySet().size() > 0){
            for (String key: map.keySet() ) {
                Object value = map.get(key);
                if(value instanceof  String){
                    sb.append(" "+key + "'"+value+"'");
                }else if(value != null){
                    sb.append(" "+key + value);
                }else{
                    sb.append(" "+key );
                }
            }
        }
        return  sb.toString();
    }


}
