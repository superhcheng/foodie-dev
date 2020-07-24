package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.supercheng.bo.UserBO;
import us.supercheng.pojo.Users;
import us.supercheng.service.UsersService;
import us.supercheng.utils.APIResponse;
import us.supercheng.utils.CookieUtils;
import us.supercheng.utils.JsonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UsersService usersService;


    @GetMapping("/isUsernameExist")
    public Object isUsernameExist(@RequestParam String username) {
        if (StringUtils.isBlank(username))
            return APIResponse.errorMsg("Empty username");

        if (this.usersService.isUsernameExist(username))
            return APIResponse.errorMsg("This username is already taken.");

        return APIResponse.ok();
    }

    @PostMapping("regist")
    public APIResponse regist(@RequestBody UserBO userBO, HttpServletRequest req, HttpServletResponse resp) {
        String username = userBO.getUsername(),
                pwd = userBO.getPassword(),
                pwdConfirm = userBO.getConfirmPassword();

        if (StringUtils.isBlank(username))
            return APIResponse.errorMsg("Empty username");

        if (StringUtils.isBlank(pwd))
            return APIResponse.errorMsg("Empty password");

        if (pwd.length() < 6)
            return APIResponse.errorMsg("Password is too short");

        if (!pwd.equals(pwdConfirm))
            return APIResponse.errorMsg("Password does not match");

        if (this.usersService.isUsernameExist(username))
            return APIResponse.errorMsg("This username is already taken.");

        Users user = this.usersService.createUser(userBO);
        this.setUsersObjCookie(user, req, resp);
        this.usersService.syncShoppingCart(user, req, resp);
        return APIResponse.ok();
    }

    @PostMapping("login")
    public APIResponse login(@RequestBody UserBO userBO, HttpServletRequest req, HttpServletResponse resp) {
        if (StringUtils.isBlank(userBO.getUsername()))
            return APIResponse.errorMsg("Empty username");

        if (StringUtils.isBlank(userBO.getPassword()))
            return APIResponse.errorMsg("Empty password");

        Users user = this.usersService.login(userBO);

        if (user == null) {
            return APIResponse.errorMsg("Incorrect Username or Password");
        }

        this.setUsersObjCookie(user, req, resp);
        this.usersService.syncShoppingCart(user, req, resp);
        return APIResponse.ok(user);
    }

    private void setUsersObjCookie(Users user, HttpServletRequest req, HttpServletResponse resp) {
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setPassword(null);
        user.setBirthday(null);
        user.setRealname(null);
        CookieUtils.setCookie(req, resp, CookieUtils.USER_COOKIE_KEY, JsonUtils.objectToJson(user), true);
    }

    @PostMapping("logout")
    public APIResponse logout(HttpServletRequest req, HttpServletResponse resp) {
        CookieUtils.deleteCookie(req, resp, CookieUtils.USER_COOKIE_KEY);
        return APIResponse.ok();
    }
}