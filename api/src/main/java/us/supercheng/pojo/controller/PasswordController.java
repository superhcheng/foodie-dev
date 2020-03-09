package us.supercheng.pojo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.supercheng.service.UsersService;
import us.supercheng.utils.APIResponse;

@RestController
@RequestMapping("password")
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
}