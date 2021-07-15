package cn.yb.auth.service;

import cn.yb.auth.service.impl.IUserService;

public class UserService implements IUserService {
    @Override
    public boolean accountLogin(String account, String password) {

        return false;
    }

    @Override
    public boolean phoneLogin(String phone, String password) {
        return false;
    }
}
