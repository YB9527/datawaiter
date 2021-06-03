package cn.yb.datawaiter.controller;

import cn.yb.datawaiter.exception.GlobRuntimeException;
import cn.yb.datawaiter.jdbc.Connect;
import cn.yb.datawaiter.jdbc.Delete;
import cn.yb.datawaiter.jdbc.JDBCUtils;
import cn.yb.datawaiter.jdbc.Select;
import cn.yb.datawaiter.jdbc.model.Column;
import cn.yb.datawaiter.jdbc.model.DatabaseConnect;
import cn.yb.datawaiter.jdbc.model.DatabaseEnum;
import cn.yb.datawaiter.model.Api;
import cn.yb.datawaiter.model.Respon;
import cn.yb.datawaiter.model.UploadFile;
import cn.yb.datawaiter.service.impl.IApiService;
import cn.yb.datawaiter.service.impl.IDatawaiterService;
import cn.yb.datawaiter.tools.Tool;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.util.FastList;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RestController
@RequestMapping(value = "/file")
public class FileController extends BasicController {

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
                uploadFile = new UploadFile(selfDir + fileName,suffix);
                uploadFile.setId(UUID.randomUUID().toString());
                uploadFiles.add(uploadFile);
                file.transferTo(dest);
            }
            return respon.ok(uploadFiles);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return respon.responError("上传失败");
    }
}
