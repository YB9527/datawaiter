package cn.yb.sys.service.impl;

import cn.yb.sys.model.Dic;
import cn.yb.sys.model.FJ;
import cn.yb.sys.model.ProjectDic;

import java.util.List;

public interface IDicService {

    List<Dic> findAllDic(ProjectDic projectDic);
}
