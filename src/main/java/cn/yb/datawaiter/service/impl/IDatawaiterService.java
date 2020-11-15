package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Mapper;
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

    /**
     *通过mapper 查询数据
     * @param mapper
     * @return
     */
    List<JSONObject> findDataByMapper(Mapper mapper);

    /**
     *  测试 mapper 的 update、 insert 、delete
     * @param mapper
     * @return
     */
    int mapperTest(Mapper mapper);
}
