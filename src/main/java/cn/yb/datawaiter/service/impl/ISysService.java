package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Param;

import java.util.List;

public interface ISysService {

    Api findApiByURLAndProjectid(String relative,String projectid);
}
