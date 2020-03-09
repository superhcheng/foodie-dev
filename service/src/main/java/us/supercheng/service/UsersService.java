package us.supercheng.service;

import org.springframework.stereotype.Service;

public interface UsersService {
    boolean isUsernameExist(String username);
}