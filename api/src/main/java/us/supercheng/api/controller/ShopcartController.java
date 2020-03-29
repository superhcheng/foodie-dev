package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.supercheng.bo.ShopcartItemBO;
import us.supercheng.utils.APIResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("shopcart")
@Controller
public class ShopcartController {

    @PostMapping("add")
    @ResponseBody
    public APIResponse addShopcart(@RequestParam String userId,
                                      @RequestBody ShopcartItemBO shopcartItem,
                                      HttpServletRequest req,
                                      HttpServletResponse resp) {
        if (StringUtils.isBlank(userId))
            return APIResponse.errorMsg("Missing required User ID");

        System.out.println(shopcartItem);

        // TODO add to Redis once we have Redis integrations implemented

        return APIResponse.ok();
    }
}