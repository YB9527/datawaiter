package cn.yb.datawaiter.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class UploadFile {
    private String id;
    private String path;
    private String extension;
    private Integer size;
    public UploadFile() {

    }

    public UploadFile(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }


}
