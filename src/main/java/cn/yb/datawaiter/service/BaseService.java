package cn.yb.datawaiter.service;

import cn.yb.datawaiter.jdbc.model.SQLParam;
import cn.yb.datawaiter.tools.ReflectTool;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import tk.mybatis.mapper.common.BaseMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseService<ID,Mapper extends BaseMapper> {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected Mapper mapper;
    public abstract  void setMapper(Mapper  mappper);



    public <B> int insert(String tableName, B entity, Class  clazz ){
        List<B> list = new ArrayList<>();
        list.add(entity);
        return  batchInsert(tableName,list,clazz);
    }

    /**
     * 批量插入
     * @param tableName
     * @param list
     * @param <B>
     * @return
     */
    public <B> int batchInsert(String tableName, List<B> list, Class  clazz ){

        //获取表结构
        String sql = "INSERT INTO "+tableName+" (";
        String sqlvalue = " VALUES ( ";
        List<Field> temp = Arrays.asList( clazz.getDeclaredFields());
        List<Field> fields = new ArrayList<>();
        //去除不是属性的字段
        for (int i = 0; i < temp.size(); i++) {
            Field field = temp.get(i);
            String name = field.getName();
            Method m =  ReflectTool.getMethod("get" + Tool.upperFirstLatter(name),clazz);
            if(m == null){
                continue;
            }
            fields.add(field);
        }

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            String name = field.getName();
            String value;
            if("wkt".equals(name)){
                value = "st_multi(ST_GeomFromText(?,4523))";
                name = "geom";
            }else{
                value = "?";
            }
            name = toLineField(name);
            if(i <  fields.size() -1){
                sqlvalue += value +",";
                sql += name + ",";
            }else{
                sqlvalue += value +" );";
                sql += name + ")";
            }
        }
        sql += sqlvalue;
        //生成插入语句
        List<Object[]> params = new ArrayList<>();
        for (B t : list) {
            if(t == null){
                continue;
            }
            Object[] itemValue = new Object[fields.size()];

            for (int i = 0; i < fields.size(); i++) {
                String name = fields.get(i).getName();
                try {
                    Method m =  ReflectTool.getMethod("get" + Tool.upperFirstLatter(name),clazz);
                    itemValue[i] = m.invoke(t,null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            params.add(itemValue);
        }
        int[] ans = jdbcTemplate.batchUpdate(sql, params);
        if(ans == null || ans.length == 0){
            return  0;
        }
        int count =0 ;
        for (int an : ans) {
            count += an;
        }
        return  count;
    }

    private String toLineField(String name) {
        char[] chars = name.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            if(Character.isUpperCase(ch)){
                sb.append("_"+ch);
            }else{
                sb.append(ch);
            }
        }
        return  sb.toString().toLowerCase();
    }

    protected int deleteData(String sql, Object[] objects) {
        int update = jdbcTemplate.update(sql, objects);
        return  update;
    }

    protected <T> List<T> queryData(String sql, Object[] params, Class<T> tClass) {
        List<T> list = jdbcTemplate.query(sql,params,new BeanPropertyRowMapper(tClass));
        return  list;
    }


    protected int findTotal(String countSQL, Object[] params) {
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap(countSQL,params);
        Object count = stringObjectMap.get("count");
        if(count != null){
            return  Integer.parseInt(count.toString());
        }
        return  0;
    }
    protected int findTotal(SQLParam sqlParam) {
        return  findTotal(sqlParam.getSql(),sqlParam.getParams());
    }

    private int findTotal(String sql, List<Object> params) {
        return  findTotal(sql,params.toArray());
    }

    protected <T> List<T> findData(String sql,Class<T> tClass) {
       return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(tClass));
    }

    protected <T> List<T> findData(String sql, List<Object> params,Class<T> tClass) {
        return findData(sql,params.toArray(), tClass);
    }
    protected <T> List<T> findData(String sql, Object[] params, Class<T> tClass){
        return jdbcTemplate.query(sql,params,new BeanPropertyRowMapper(tClass));
    }

    /**
     * 当没有id时才设置id
     * @param data
     */
    protected void setIdIsNull(Object data) {
        Method getId = ReflectTool.getMethod("getId", data.getClass());

        try {
            if(getId.invoke(data,null) == null){
                Method setId = ReflectTool.getMethod("setId", data.getClass());
                setId.invoke(data,UUID.randomUUID().toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T> T   findDataById(ID id,Class<T> tClass){
        Object obj =  mapper.selectByPrimaryKey(id);
        if(obj == null){
            return  null;
        }
        return JSON.parseObject(JSON.toJSONString(obj),tClass);
    }


    public <T> int updateData(T data) {
        return mapper.updateByPrimaryKey(data);
    }

    public <T> int saveData(T data) {
        this.setIdIsNull(data);
        return mapper.insert(data);
    }

    public int deleteByPrimaryKey(ID id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
