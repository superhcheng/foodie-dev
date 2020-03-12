package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.bo.UserBO;
import us.supercheng.mapper.UsersMapper;
import us.supercheng.pojo.Users;
import us.supercheng.service.UsersService;
import us.supercheng.utils.DateUtil;
import us.supercheng.utils.MD5Utils;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional()
    @Override
    public boolean isUsernameExist(String username) {
        Example userExp = new Example(Users.class);
        Example.Criteria userCriteria = userExp.createCriteria();
        userCriteria.andEqualTo("username", username);
        Users users = this.usersMapper.selectOneByExample(userExp);
        return users != null;
    }

    @Override
    public Users createUser(UserBO user) {
        Users ret = new Users();

        ret.setUsername(user.getUsername());
        ret.setNickname(user.getUsername());
        try {
            ret.setPassword(MD5Utils.getMD5Str(user.getPassword()));
        } catch (Exception ex) {}
        ret.setBirthday(DateUtil.stringToDate("1900-1-1"));

        return ret;
    }
}