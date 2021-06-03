package cn.yb.datawaiter.controller;

import lombok.Data;

@Data
public class UploadFile {
    private String id;
    private String path;
    private String extension;

    public UploadFile() {

    }

    public UploadFile(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }
}
