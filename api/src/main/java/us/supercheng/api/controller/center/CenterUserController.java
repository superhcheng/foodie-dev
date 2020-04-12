package us.supercheng.api.controller.center;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import us.supercheng.bo.center.CenterUserBO;
import us.supercheng.pojo.Users;
import us.supercheng.service.center.CenterUsersService;
import us.supercheng.utils.APIResponse;
import us.supercheng.utils.CookieUtils;
import us.supercheng.utils.JsonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("userInfo")

public class CenterUserController {

    @Autowired
    private CenterUsersService centerUsersService;

    @PostMapping("update")
    public Object update(@RequestParam String userId,
                         @RequestBody @Valid CenterUserBO userBO,
                         BindingResult bindingResult,
                         HttpServletRequest req, HttpServletResponse resp) {
        if (StringUtils.isBlank(userId))
            return APIResponse.errorMsg("Missing required field userId");

        if (bindingResult.hasErrors()) {
            return APIResponse.errorMap(this.resToMap(bindingResult));
        }

        Users ret = this.centerUsersService.update(userId, userBO);
        this.setNullProperty(ret);
        CookieUtils.setCookie(req, resp, CookieUtils.USER_COOKIE_KEY, JsonUtils.objectToJson(ret), true);
        return APIResponse.ok(ret);
    }

    private void setNullProperty(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
    }


    private Map<String, String> resToMap(BindingResult result) {
        Map<String, String> ret = new HashMap<>();

        for (FieldError e : result.getFieldErrors())
            ret.put(e.getField(), e.getDefaultMessage());

        return ret;
    }
}
