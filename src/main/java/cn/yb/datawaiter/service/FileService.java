package cn.yb.datawaiter.service;

import cn.yb.datawaiter.jdbc.Insert;
import cn.yb.datawaiter.jdbc.SystemConnect;
import cn.yb.datawaiter.model.UploadFile;
import cn.yb.datawaiter.service.impl.IFileService;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Component
public class FileService implements IFileService {
    @Override
    public int save(List<UploadFile> uploadFiles) {
        int count = 0;
        try {
            Connection conn = SystemConnect.getConn();
            conn.setAutoCommit(false);

            count = Insert.insertManyPos(conn, uploadFiles);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
