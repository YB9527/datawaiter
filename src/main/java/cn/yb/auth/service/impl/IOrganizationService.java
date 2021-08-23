package cn.yb.auth.service.impl;

import cn.yb.auth.model.Organization;
import cn.yb.auth.model.Position;

import java.util.List;

public interface IOrganizationService {

    void add(Organization organization);

    int edit(Organization organization);

    List<Organization> findByProjectid(String projectid);

    int delete(Organization organization);
}
