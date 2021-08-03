package cn.yb.sys.model;

import lombok.Data;

@Data
public class Dic {
    private  String id;
    /**
     * 字典类别
     */
    private  String dicgroup;
    private  String dicgrouplabel;
    private  String key;
    private  String value;
    private  int seq;
}
