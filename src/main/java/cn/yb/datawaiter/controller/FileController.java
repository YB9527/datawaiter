package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.model.UploadFile;
import cn.yb.datawaiter.service.impl.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RestController
@RequestMapping(value = "/file")
public class FileController extends BasicController {

    @Autowired
    private IFileService fileService;
    @Value(value = "${uploadconfig.dir}")
    private String uploadDir;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    @GetMapping("/upload2")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Respon upload(HttpServletRequest request,@RequestParam("dir") String dir,@RequestParam("dirs") List<String> dirs) {


        Respon respon = startRespon();
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        // List<String> dirs = null;
        //String dir = null;
        List<UploadFile> uploadFiles = new ArrayList<>();
        UploadFile uploadFile = null;
        if (files.isEmpty()) {
            return respon.responError("上传失败，请选择文件");
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String fileName = file.getOriginalFilename();
                String selfDir = "";
                if(dirs != null && dirs.size() == files.size()){
                    selfDir =  dirs.get(i)+"\\";
                }else if(dir != null && !dir.trim().isEmpty()){
                    selfDir =  dir+"\\";
                }
                File dirFile = new File(uploadDir + selfDir);
                if(!dirFile.exists()){
                    dirFile.mkdirs();
                }
                File dest = new File(uploadDir+ selfDir + fileName);
                String suffix = "";
                if(fileName.contains(".")){
                    suffix = fileName.substring(fileName.lastIndexOf(".")+1);
                }
                uploadFile = new UploadFile("\\"+selfDir + fileName,suffix);
                uploadFile.setId(UUID.randomUUID().toString());
                uploadFile.setSize((int)(file.getSize() / 1024));
                uploadFiles.add(uploadFile);
                file.transferTo(dest);
            }
            int  saveCount = fileService.save(uploadFiles);
            if(saveCount > 0){
                return respon.ok(uploadFiles);
            }
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return respon.responError("上传失败");
    }
}
