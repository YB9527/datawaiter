package cn.yb.datawaiter.jdbc.model;

import cn.yb.datawaiter.tools.Tool;


import java.util.ArrayList;
import java.util.List;

public abstract class SQLBuilder {
    protected String tableName ;
    protected BuilderEnum builderEnum;
//    protected LinkedHashMap<String,Object> map = new LinkedHashMap<>();
    protected List<FieldWhere> fieldWheres = new ArrayList<>();

    protected   SQLBuilder(String tableName,BuilderEnum builderEnum){
        this.builderEnum = builderEnum;
        this.tableName = tableName;
    }


    public String getSQL() {
        StringBuilder sb = new StringBuilder();
        switch (builderEnum) {
            case DELETE:
                sb.append("DELETE  FROM " +tableName+" ");
                break;
            case SELECT:
                sb.append("SELECT * FROM " +tableName+" ");
                break;
        }
        String where = Select.getWhereSQL(fieldWheres);;


        if(!Tool.isEmpty(where)){
            sb.append(" WHERE " );
            sb.append(where);
        }
        return  sb.toString();
    }



    public <T extends SQLBuilder> T setWhereFiled(String field, Object value){
        fieldWheres.add(new FieldWhere(null,field,value));
        return  (T)this;
    }

    public <T extends SQLBuilder> T setWhereFiled(FieldWhere fieldWhere){
       fieldWheres.add(fieldWhere);
        return  (T)this;
    }

    public <T extends SQLBuilder> T  setWhereFiled_LikeOr(String smybol, String field,List values){
        if(Tool.isEmpty(values)){
            return  (T)this;
        }
        if(!Tool.isEmpty(smybol)){
            this.setWhereFiled(smybol,null);
        }
        this.setWhereFiled("(",null);
        for (int i = 0; i < values.size(); i++) {
            if(i != 0){
                this.setWhereFiled(" OR "+field +" LIKE " ,values.get(i));
            }else{
                this.setWhereFiled( field + " LIKE ",values.get(i));
            }
        }
        this.setWhereFiled(")",null);
        return  (T)this;
    }
    public <T extends SQLBuilder> T setWhereFiled_LikeOr(String value, String ...searckey) {
        if(value == null){
            return  (T)this;
        }
        if(searckey !=null && searckey.length > 0){
            this.setWhereFiled("(",null);
            for (int i = 0; i < searckey.length; i++) {
                if(i != 0){
                    this.setWhereFiled(" OR "+searckey[i] +" LIKE " ,value);
                }else{
                    this.setWhereFiled( searckey[i] + " LIKE ",value);
                }
            }
            this.setWhereFiled(")",null);
        }
        return  (T)this;
    }




    public  <T extends SQLBuilder> T  setWhereMultFiled_EqOr(String symbol,String filed, List values){
        if(Tool.isEmpty(filed) || Tool.isEmpty(values)){
            return  (T)this;
        }
        if(fieldWheres.size() > 0 && symbol != null){
            this.setWhereFiled(symbol,null);
        }
        this.setWhereFiled("( "+ filed+" in (",null);

        for (int i = 0; i < values.size(); i++) {
            if(i == 0){
                this.setWhereFiled( "",values.get(i));

            }else{
                this.setWhereFiled(",",values.get(i));
            }
        }
        this.setWhereFiled(") )",null);
        return  (T)this;
    }
    public  <T extends SQLBuilder> T  setWhereFiledNotNull(String symbol,String filed, Object obj){
        if(obj == null ){
            return  (T)this;
        }
        if(fieldWheres.size() > 0 && symbol != null){
            this.setWhereFiled(symbol,null);
        }
        return setWhereFiled(filed,obj);
    }



}
