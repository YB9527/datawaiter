package cn.yb.datawaiter.service;

import cn.yb.datawaiter.mapper.ApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestService extends  BaseService<String,ApiMapper>{


    public void fun() {
        
    }


    @Autowired
    @Override
    public void setMapper(ApiMapper mappper) {
        this.mapper = mappper;
    }
}
