package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.mapper.UsersMapper;
import us.supercheng.pojo.Users;
import us.supercheng.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public boolean isUsernameExist(String username) {
        Example userExp = new Example(Users.class);
        Example.Criteria userCriteria = userExp.createCriteria();
        userCriteria.andEqualTo("username", username);
        Users users = this.usersMapper.selectOneByExample(userExp);
        return users != null;
    }
}