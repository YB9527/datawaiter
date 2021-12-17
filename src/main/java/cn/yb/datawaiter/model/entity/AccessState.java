package cn.yb.datawaiter.model.entity;

import lombok.Data;


public enum AccessState {
    Normal("Normal","正常"),
    NotStart("NotStart","未开始" ),

    Stop("Stop","停止使用"),
    Exception("Exception","异常");

    public String label;
    public String id;
    // 构造方法
     private AccessState(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}