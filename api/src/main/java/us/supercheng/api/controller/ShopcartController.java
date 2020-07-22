package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.supercheng.bo.ShopcartItemBO;
import us.supercheng.utils.APIResponse;
import us.supercheng.utils.CookieUtils;
import us.supercheng.utils.JsonUtils;
import us.supercheng.utils.RedisOperator;
import us.supercheng.vo.ShopcartItemVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("shopcart")
@Controller
public class ShopcartController {

    @Autowired
    private RedisOperator redisOperator;

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

        String key = "shopping_cart:" + userId,
               shoppingCart = this.redisOperator.get(key),
               itemId = shopcartItem.getSpecId(),
               jsonStr = null;
        List<ShopcartItemBO> list = null;

        if (StringUtils.isBlank(shoppingCart)) {
            list = new ArrayList<>();
            list.add(shopcartItem);
        } else {
            list = JsonUtils.jsonToList(shoppingCart, ShopcartItemBO.class);
            boolean duplicate = false;
            for (ShopcartItemBO bo : list) {
                if (bo.getSpecId().equalsIgnoreCase(itemId)) {
                    bo.setBuyCounts(bo.getBuyCounts() + shopcartItem.getBuyCounts());
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                list.add(shopcartItem);
            }
        }

        jsonStr = JsonUtils.objectToJson(list);
        this.redisOperator.set(key, jsonStr);
        CookieUtils.setCookie(req, resp, CookieUtils.SHOPCART_COOKIE_KEY, jsonStr, true);

        return APIResponse.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public APIResponse delShopcartItemBySpecId(@RequestParam String userId,
                                               @RequestParam String itemSpecId,
                                               HttpServletRequest req,
                                               HttpServletResponse resp) {
        if (StringUtils.isBlank(userId))
            return APIResponse.errorMsg("Missing required fields User ID and/or Item Spec ID");

        // TODO delete from Redis once we have Redis integrations implemented
        String key = "shopping_cart:" + userId,
                shoppingCart = this.redisOperator.get(key),
                jsonStr = null;
        List<ShopcartItemBO> list = null;

        if (StringUtils.isBlank(shoppingCart)) {
            CookieUtils.deleteCookie(req, resp, CookieUtils.SHOPCART_COOKIE_KEY);
            return APIResponse.errorMsg("Shopping cart not found in redis and reset cookie");
        } else {
            int delIdx = -1;
            list = JsonUtils.jsonToList(shoppingCart, ShopcartItemBO.class);

            for (int i=list.size()-1; i>-1 && delIdx == -1; i++)
                if (list.get(i).getSpecId().equalsIgnoreCase(itemSpecId))
                    delIdx = i;

            list.remove(delIdx);
        }

        jsonStr = JsonUtils.objectToJson(list);
        this.redisOperator.set(key, jsonStr);
        CookieUtils.setCookie(req, resp, CookieUtils.SHOPCART_COOKIE_KEY, jsonStr, true);

        return APIResponse.ok();
    }
}