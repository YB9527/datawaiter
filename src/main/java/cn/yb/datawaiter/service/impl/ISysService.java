package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.entity.ApiEntity;

public interface ISysService {

    ApiEntity findApiByURLAndProjectid(String relative, String projectid);
}
