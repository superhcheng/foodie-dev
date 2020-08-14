package us.supercheng.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import us.supercheng.pojo.Users;
import us.supercheng.service.UsersService;
import us.supercheng.sso.service.IAuthService;
import us.supercheng.utils.JsonUtils;
import us.supercheng.utils.RedisOperator;
import us.supercheng.vo.UsersVO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {

    private final String USER_CAS_SESSION_KEY = "user_cas_session:";
    private final String USER_CAS_TKT_KEY = "user_cas_tkt:";
    private final String USER_CAS_TEMP_TKT_KEY = "user_cas_tkt:";
    private final String USER_COOKIE_TKT = "user_cookie_tKt:";

    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean checkCASSSO() {
        return false;
    }

    @Transactional
    @Override
    public boolean createCASSession(String username, String password, String retUrl, Model model, HttpServletRequest req, HttpServletResponse resp) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errMsg", "Username and/or Password is empty.");
            return false;
        }

        Users user = this.usersService.validateLogin(username, password);

        if (user == null) {
            model.addAttribute("errMsg", "Username and/or Password does not match or does not exist.");
            return false;
        }


        String redisCASUUID = UUID.randomUUID().toString().trim(),
               userId = user.getId();
        this.usersService.removeUsersPII(user);
        UsersVO usersVO = this.usersService.convertUsersToUsersVOWithToken(user, redisCASUUID);

        // UserID - UsersVO
        this.redisOperator.set(USER_CAS_SESSION_KEY + userId, JsonUtils.objectToJson(usersVO));

        // Create TKT
        String redisCASTKTUUID = UUID.randomUUID().toString().trim();
        this.redisOperator.set(USER_CAS_TKT_KEY + redisCASTKTUUID, userId);
        this.setCookie(resp, USER_COOKIE_TKT, redisCASTKTUUID);

        // todo
        return false;
    }

    @Override
    public void createCASTKT() {

    }

    @Override
    public void createCASTempTKT() {

    }

    void setCookie(HttpServletResponse resp, String key, String val) {
        Cookie cookie = new Cookie(key, val);
        cookie.setPath("/");
        cookie.setDomain("sso.com");
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
    }

    void remCookie(HttpServletResponse resp, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setDomain("sso.com");
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
    }

    String getCookie(HttpServletRequest req, String key) {
        Cookie[] cookies = req.getCookies();

        for (Cookie c :cookies)
            if (c.getName().equals(key))
                return c.getValue();

        return null;
    }
}
