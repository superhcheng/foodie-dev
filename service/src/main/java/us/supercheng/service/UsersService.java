package us.supercheng.service;

import us.supercheng.bo.UserBO;
import us.supercheng.pojo.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UsersService {
    boolean isUsernameExist(String username);
    Users createUser(UserBO user);
    Users login(UserBO user);
    void syncShoppingCart(Users user, HttpServletRequest req, HttpServletResponse resp);
}