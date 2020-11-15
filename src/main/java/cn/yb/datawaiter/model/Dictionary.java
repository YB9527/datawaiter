package cn.yb.datawaiter.model;

import lombok.Data;

@Data
public class Dictionary {
    private  String id;
    /**
     * 字典类别
     */
    private  String type;
    private  String dicKey;
    private  String dicValue;
    private  String levelManagerId;
}
