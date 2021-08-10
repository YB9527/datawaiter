package cn.yb.sys.service;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.SelectBuild;
import cn.yb.datawaiter.tools.ReflectTool;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.model.Dic;
import cn.yb.sys.model.FJ;
import cn.yb.sys.model.ProjectDic;
import cn.yb.sys.service.impl.IDicService;
import cn.yb.sys.service.impl.IFJService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicService implements IDicService {


    @Override
    public List<Dic> findAllDic(ProjectDic projectDic) {
        Connection conn = Connect.getSQLConnection(projectDic.getDatabaseconnectid());
        List<Dic> dicArray =  SelectBuild.newInstance(projectDic.getDictablename())
                .order("seq")
                .build(conn,Dic.class);
        List<Dic> parents = new ArrayList<>();
        if(!Tool.isEmpty(dicArray)){
            for (int i = 0; i < dicArray.size(); i++) {
                Dic parent = dicArray.get(i);
                if (Tool.isEmpty( parent.getKey())){
                    parents.add(parent);
                    dicArray.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < parents.size(); i++) {
                Dic parent = parents.get(i);
                parent.setChildren(new ArrayList<>());
                for (int j = 0; j < dicArray.size(); j++) {
                    if(parent.getDicgroup().equals(dicArray.get(j).getDicgroup())){
                        parent.getChildren().add(dicArray.get(j));
                        dicArray.remove(j);
                        j--;
                    }
                }
            }


        }
        return parents;
    }
}
