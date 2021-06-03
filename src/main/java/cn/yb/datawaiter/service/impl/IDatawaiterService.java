package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Mapper;
import cn.yb.datawaiter.model.Param;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IDatawaiterService {



    /**
     *通过mapper 查询数据
     * @param mapper
     * @return
     */
    List<JSONObject> findDataByMapper(Mapper mapper);


    List<JSONObject> findDataByMapper(Api api, Map<String, String> paramMap);

    int handleData(Api api, Map<String, String> paramMap);
    int handleData(Api api, JSONObject jsonObject);
}
