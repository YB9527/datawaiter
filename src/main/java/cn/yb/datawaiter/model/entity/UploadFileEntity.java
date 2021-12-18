package cn.yb.datawaiter.model.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import java.util.UUID;

@Data
@Table(name = "file")
public class UploadFileEntity {
    private String id;
    private String path;
    private String extension;
    private Integer size;
    public UploadFileEntity() {

    }

    public UploadFileEntity(String path, String extension) {
        this.path = path.substring(1);
        this.extension = extension;
    }


}
