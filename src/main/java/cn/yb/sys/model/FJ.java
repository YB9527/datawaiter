package cn.yb.sys.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FJ {
    private  String id;
    private  String extendname;
    private  String path;
    private  double size;
    private  String objectid;
    private  String customname;
    private  String projectid;
    private int isdelete;

    // 文件
    @JSONField(serialize=false)
    private MultipartFile file;

}
