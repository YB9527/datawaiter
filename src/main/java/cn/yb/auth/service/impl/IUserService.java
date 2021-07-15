package cn.yb.auth.service.impl;

public interface IUserService {
    boolean accountLogin(String account, String password);

    boolean phoneLogin(String phone, String password);
}
