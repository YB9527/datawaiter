package cn.yb.datawaiter.service.impl;

import com.alibaba.fastjson.JSONObject;

public interface ILevelService {
    JSONObject findDataById( String id);

    void findParent(JSONObject json);
}
