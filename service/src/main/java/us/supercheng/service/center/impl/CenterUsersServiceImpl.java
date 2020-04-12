package us.supercheng.service.center.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.supercheng.mapper.UsersMapper;
import us.supercheng.pojo.Users;
import us.supercheng.service.center.CenterUsersService;

@Service
public class CenterUsersServiceImpl implements CenterUsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users getUserByUserId(String userId) {
        Users users = new Users();
        users.setId(userId);
        return this.usersMapper.selectByPrimaryKey(users);
    }
}
