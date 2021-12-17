package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.model.entity.Respon;
import cn.yb.datawaiter.model.entity.UploadFileEntity;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@Controller
@RestController
@RequestMapping(value = "/file")
public class FileController extends BasicController {

    private String uploadDir;

    @Value("#{${uploadconfig.dir}}")
    public void dirMap(Map<String,String> dirMap){
        String ip = Tool.getInterIP();
        String key ;
        if(ip.startsWith("172.18.254.48")){
            key = "out";
        }else{
            key = "in";
        }
        this.uploadDir = dirMap.get(key);
    }



    @RequestMapping(value = "/test")
    public Respon findApiLevel() throws SQLException, ClassNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        String ip="";
        DatabaseEnum databaseEnum=DatabaseEnum.mysql;
        String databaseName="test";
        String name = "root";
        String password = "123456";
        DatabaseConnect dc = new DatabaseConnect(ip,databaseEnum,databaseName,name,password);
        Connection systemConn = Connect.getSQLConnection(dc);
        Statement statement = systemConn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        List<Column> cols = Connect.getTableColumn(rsmd);
        JSONObject jsonObject;
        while (resultSet.next()) {
            jsonObject = new JSONObject();
            jsonObjects.add(jsonObject);
            for (Column column : cols) {
                jsonObject.put(column.getColumnLabel(), resultSet.getObject(column.getColumnLabel()));
            }
        }
        return startRespon().ok(jsonObjects);
    }




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
        List<UploadFileEntity> uploadFileEntities = new ArrayList<>();
        UploadFileEntity uploadFileEntity = null;
        if (files.isEmpty()) {
            return respon.responError("上传失败，请选择文件");
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                String uuid = UUID.randomUUID().toString();
                MultipartFile file = files.get(i);
                String fileName = uuid+"_" +file.getOriginalFilename();
                String selfDir = "";
                if(dirs != null && dirs.size() == files.size()){
                    selfDir =  dirs.get(i)+"/";
                }else if(dir != null && !dir.trim().isEmpty()){
                    selfDir =  dir+"/";
                }
                File dirFile = new File(uploadDir + selfDir);
                if(!dirFile.exists()){
                    boolean bl = dirFile .mkdirs();
                    int a =1;
                }
                File dest = new File(uploadDir+ selfDir + fileName);

                String suffix = "";
                if(fileName.contains(".")){
                    suffix = fileName.substring(fileName.lastIndexOf(".")+1);
                }
                uploadFileEntity = new UploadFileEntity('/'+selfDir + fileName,suffix);
                uploadFileEntity.setId(uuid);
                uploadFileEntities.add(uploadFileEntity);
                file.transferTo(dest);
                //System.out.println("path:"+dest.getAbsolutePath());
            }
            return respon.ok(uploadFileEntities);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return respon.responError("上传失败");
    }
}
