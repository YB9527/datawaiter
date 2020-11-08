package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface IDatawaiterService {




    List<JSONObject> findDataByApi(Api api);
}
