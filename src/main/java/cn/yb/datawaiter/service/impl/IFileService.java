package cn.yb.datawaiter.service.impl;

import cn.yb.datawaiter.model.UploadFile;

import java.util.List;

public interface IFileService {
    int save(List<UploadFile> uploadFiles);
}
