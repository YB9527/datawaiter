package cn.yb.sys.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目实体类Project")
public class Project {

    private  String id;
    private  String name;
    private  String url;
    private  String description;
    private  Integer seq;
}
