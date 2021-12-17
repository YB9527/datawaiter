package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.entity.UploadFileEntity;

import java.util.List;

public interface IFileService {
    int save(List<UploadFileEntity> uploadFileEntities);
}
