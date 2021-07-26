package cn.yb.sys.service;

import cn.yb.datawaiter.controller.FileController;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.model.UploadFile;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.FJ;
import cn.yb.sys.service.impl.IFJService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FJService implements IFJService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FJService.class);
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

    @Override
    public List<FJ> findImageByObjectidAndCustomname(String objectid, String customname) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("objectid=",objectid);
        map.put(" AND customname=",customname);
        map.put(" AND isdelete=",0);
        String sql =  Select.getSQL(FJ.class,map);

        return Select.findDataBySQL(SystemConnect.getConn(),sql,FJ.class);
    }

    @Override
    public int setAttributeAndSave(List<FJ> fjArray) {
        try {
            for (int i = 0; i < fjArray.size(); i++) {
                //String uuid = UUID.randomUUID().toString();
                FJ fj = fjArray.get(i);
                MultipartFile file = fj.getFile();
                String path = fj.getPath();
                if(!path.startsWith("\\")){
                    path = "\\"+path;
                }
                int index = path.lastIndexOf("\\");
                String dir =  path.substring(0,index+1);
                path =dir+fj.getId()+"_"+path.substring(index+1,path.length());

                fj.setPath(path);
                fj.setIsdelete(0);
                File dirFile = new File(uploadDir+dir);
                if(!dirFile.exists()){
                    boolean bl = dirFile .mkdirs();
                    int a =1;
                }
                File dest = new File(uploadDir + path);
                file.transferTo(dest);
            }
            return Insert.insertManyPosInService(SystemConnect.getConn(),fjArray);
            //保存附件
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return  0;
    }

    @Override
    public int delete(List<FJ> fjList) {
        for (FJ fj:fjList
             ) {
            fj.setIsdelete(1);
        }
        return Update.updateManyDataPosInService(SystemConnect.getConn(),fjList);
    }

}
