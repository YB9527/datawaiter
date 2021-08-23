package cn.yb.auth.service;

import cn.yb.auth.model.Organization;
import cn.yb.auth.model.Position;
import cn.yb.auth.model.User;
import cn.yb.auth.service.impl.IOrganizationService;
import cn.yb.auth.service.impl.IPositionService;
import cn.yb.datawaiter.jdbc.*;
import cn.yb.datawaiter.jdbc.model.SelectBuild;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrganizationService implements IOrganizationService {

    private Connection getSysConnection(){
        return  SystemConnect.getConn();
    }


    @Override
    public void add(Organization organization) {
         Insert.insertManyPosInService(getSysConnection(),organization);
    }

    @Override
    public int edit(Organization organization) {
        return JDBCUtils.editPo(SystemConnect.getConn(),organization);
    }

    @Override
    public List<Organization> findByProjectid(String projectid) {
        return SelectBuild.newInstance(Organization.class)
                .setWhereFiled("projectid = ",projectid)
                .order("ORDER BY  level")
                .build(SystemConnect.getConn(),Organization.class);
    }

    @Override
    public int delete(Organization organization) {
       return  Delete.deleteDataInServerice(SystemConnect.getConn(),organization);
    }
}
