package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.Level;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;

public interface ILevelService {
    JSONObject findDataById( String id);

    void findParent(JSONObject json);
}
