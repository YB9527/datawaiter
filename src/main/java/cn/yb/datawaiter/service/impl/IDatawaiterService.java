package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.entity.ApiEntity;
import cn.yb.datawaiter.model.entity.MapperEntity;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IDatawaiterService {



    /**
     *通过mapper 查询数据
     * @param mapperEntity
     * @return
     */
    List<JSONObject> findDataByMapper(MapperEntity mapperEntity);


    List<JSONObject> findDataByMapper(ApiEntity apiEntity, Map<String, String> paramMap);

    int handleData(ApiEntity apiEntity, Map<String, String> paramMap);
    int handleData(ApiEntity apiEntity, JSONObject jsonObject);
}
