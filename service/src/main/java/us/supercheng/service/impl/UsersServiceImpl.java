package us.supercheng.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.bo.UserBO;
import us.supercheng.enums.Sex;
import us.supercheng.mapper.UsersMapper;
import us.supercheng.pojo.Users;
import us.supercheng.service.UsersService;
import us.supercheng.utils.DateUtil;
import us.supercheng.utils.MD5Utils;

import java.util.Date;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUsernameExist(String username) {
        Example userExp = new Example(Users.class);
        Example.Criteria userCriteria = userExp.createCriteria();
        userCriteria.andEqualTo("username", username);
        Users users = this.usersMapper.selectOneByExample(userExp);
        return users != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO user) {
        Users ret = new Users();

        ret.setId(this.sid.nextShort());
        ret.setUsername(user.getUsername());
        ret.setNickname(user.getUsername());
        ret.setSex(Sex.Secret.type);
        ret.setFace("PLACEHOLDER");
        try {
            ret.setPassword(MD5Utils.getMD5Str(user.getPassword()));
        } catch (Exception ex) {}
        ret.setBirthday(DateUtil.stringToDate("1900-1-1"));
        Date now = new Date();
        ret.setCreatedTime(now);
        ret.setUpdatedTime(now);

        this.usersMapper.insert(ret);

        return ret;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users login(UserBO user) {
        Example userExp = new Example(Users.class);
        Example.Criteria criteria = userExp.createCriteria();

        criteria.andEqualTo("username", user.getUsername());
        try {
            criteria.andEqualTo("password", MD5Utils.getMD5Str(user.getPassword()));
        } catch (Exception ex) {
            throw new RuntimeException("MD5 String Conversion Exception");
        }

        return this.usersMapper.selectOneByExample(userExp);
    }


}