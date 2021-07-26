package cn.yb.sys.service.impl;

import cn.yb.sys.model.FJ;

import java.util.List;

public interface IFJService  {
    List<FJ> findImageByObjectidAndCustomname(String objectid, String customname);

    int setAttributeAndSave(List<FJ> fjArray);

    int delete(List<FJ> fjList);
}
