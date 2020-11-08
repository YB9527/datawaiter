package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface IDatawaiterService {


    /**
     * 查找数据
     * @param api
     * @return
     */
    List<JSONObject> findDataByApi(Api api);

    /**
     * 增删改 操作
     * @param api
     * @return
     */
    int handleData(Api api);
}
