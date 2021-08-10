package cn.yb.sys.service.impl;

import cn.yb.datawaiter.jdbc.model.Field;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface IFieldService {
    void saveOrUpdateField(List<Field> fieldarray);

    List<JSONObject> findTableField(String projectid, String databaseid, String tableName);
}
