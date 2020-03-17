package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.supercheng.bo.UserBO;
import us.supercheng.service.UsersService;
import us.supercheng.utils.APIResponse;

@RestController
@RequestMapping("passport")
public class PasswordController {

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
    public APIResponse regist(@RequestBody UserBO userBO) {
        String userName = userBO.getUsername(),
                pwd = userBO.getPassword(),
                pwdConfirm = userBO.getConfirmPassword();

        if (StringUtils.isBlank(userName))
            return APIResponse.errorMsg("Empty username");

        if (StringUtils.isBlank(pwd))
            return APIResponse.errorMsg("Empty password");

        if (pwd.length() < 7)
            return APIResponse.errorMsg("Password is too short");

        if (!pwd.equals(pwdConfirm))
            return APIResponse.errorMsg("Password does not match");

        if (this.usersService.isUsernameExist(userName))
            return APIResponse.errorMsg("This username is already taken.");

        this.usersService.createUser(userBO);

        return APIResponse.ok();
    }
}