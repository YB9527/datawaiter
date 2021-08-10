package cn.yb.sys.service;

import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.Field;
import cn.yb.datawaiter.jdbc.model.FieldDic;
import cn.yb.datawaiter.jdbc.model.SelectBuild;
import cn.yb.datawaiter.model.UploadFile;
import cn.yb.datawaiter.service.impl.IFileService;
import cn.yb.datawaiter.tools.Tool;
import cn.yb.sys.service.impl.IFieldService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FieldService implements IFieldService {


    @Override
    public void saveOrUpdateField(List<Field> fieldarray) {
        List<Field> deleteField = new ArrayList<>();
        List<Field> updateField = new ArrayList<>();
        List<Field> saveField = new ArrayList<>();
        List<FieldDic> saveFieldDic = new ArrayList<>();
        for(Field field : fieldarray){
            FieldDic fieldDic = field.getFielddic();
            if( fieldDic == null ){
                if(!Tool.isEmpty(field.getId())){
                    deleteField.add(field);
                }

            }else{
                String id = findFieldDicId(fieldDic.getProjectdicid(),fieldDic.getDicgroup());
                if(id == null){
                    id = UUID.randomUUID().toString();
                    fieldDic.setId( id);
                    saveFieldDic.add(fieldDic);
                }
                field.setFielddicid(id);
                if(Tool.isEmpty(field.getId())){
                    field.setId( UUID.randomUUID().toString());
                    saveField.add(field);
                }else{
                    updateField.add(field);
                }
            }
        }
        Connection conn = SystemConnect.getConn();
        JDBCUtils.startTransaction(conn);
        Delete.deleteDataByPri(conn,deleteField);
        Update.updateManyDataPos(conn,updateField);
        Insert.insertManyPos(conn,saveField);
        Insert.insertManyPos(conn,saveFieldDic);
        JDBCUtils.conmitTransaction(conn);
    }

    @Override
    public List<JSONObject> findTableField(String projectid, String databaseid, String tablename) {
        String sql = "SELECT * FROM  (SELECT * FROM field WHERE projectid = '"+projectid+"' AND databaseid = '"+databaseid+"' AND tablename = '"+tablename+"' ) field\n" +
                "\tLEFT JOIN fielddic\n" +
                "\tON field.fielddicid = fielddic.id";
        List<JSONObject> fields = Select.findDataBySQL(SystemConnect.getConn(),sql);


        return fields;
    }


    private String findFieldDicId(String projectdicid, String dicgroup) {
          List<FieldDic> fieldDics = SelectBuild
                    .newInstance(FieldDic.class)
                    .setWhereFiled("projectdicid = ",projectdicid)
                    .setWhereFiled(" AND dicgroup = ",dicgroup)
                    .build(SystemConnect.getConn(),FieldDic.class);
          if(!Tool.isEmpty(fieldDics) ){
              return  fieldDics.get(0).getId();
          }
          return  null;
    }
}
