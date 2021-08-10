package cn.yb.sys.model;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDic {
    private String id;
    private String projectid;
    private String databaseconnectid;
    private String dictablename;
    private String label;

    private List<Dic> dicArray;
}
