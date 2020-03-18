package us.supercheng.service;

import us.supercheng.bo.UserBO;
import us.supercheng.pojo.Users;

public interface UsersService {
    boolean isUsernameExist(String username);
    Users createUser(UserBO user);
    Users login(UserBO user);
}