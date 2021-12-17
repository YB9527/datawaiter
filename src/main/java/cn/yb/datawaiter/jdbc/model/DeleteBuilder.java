package cn.yb.datawaiter.jdbc.model;

import java.util.LinkedHashMap;

public class DeleteBuilder extends  SQLBuilder{

    private String sql ;
    private String order ="" ;
    private  int pagenum;
    private  int pagecount;
    private LinkedHashMap<String,Object> map = new LinkedHashMap<>();

    private DeleteBuilder(String tableName) {
        super(tableName, BuilderEnum.DELETE);
    }

    public    static DeleteBuilder newInstance(String tableName ){
        return new DeleteBuilder(tableName);
    }

    public   static<T> DeleteBuilder newInstance(Class<T> tClass ){
        String tableName = tClass.getSimpleName().toLowerCase();
        return new DeleteBuilder(tableName);
    }


    @Override
    public DeleteBuilder setWhereFiled(String field, Object value) {
        return super.setWhereFiled(field, value);
    }

    @Override
    public DeleteBuilder setWhereFiled_LikeOr(String value, String... searckey) {
        return super.setWhereFiled_LikeOr(value, searckey);
    }


}
