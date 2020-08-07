package us.supercheng.service;

import us.supercheng.bo.UserBO;
import us.supercheng.pojo.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UsersService {
    boolean isUsernameExist(String username);
    Users createUser(UserBO user);
    Users login(UserBO user);
    String createUserSession(Users users);
    boolean hasUserSession(String userId, String userToken);
    void delUserSession(String userId);
    void syncShoppingCart(Users user, HttpServletRequest req, HttpServletResponse resp);
}