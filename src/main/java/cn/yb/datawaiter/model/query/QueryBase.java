package cn.yb.datawaiter.model.query;

import lombok.Data;

@Data
public class QueryBase {
    private String searchkey;
    private  int pagenum;
    private  int pagecount;
    private  String order;

}