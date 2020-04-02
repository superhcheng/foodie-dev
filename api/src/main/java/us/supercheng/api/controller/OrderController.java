package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.supercheng.bo.SubmitOrderBO;
import us.supercheng.enums.PaymentType;
import us.supercheng.service.OrderService;
import us.supercheng.utils.APIResponse;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("create")
    public APIResponse createOrder(@RequestBody SubmitOrderBO submitOrderBO) {
        if (submitOrderBO == null)
            return APIResponse.errorMsg("Missing request body");

        if (StringUtils.isBlank(submitOrderBO.getAddressId()))
            return APIResponse.errorMsg("Missing Address ID");

        if (StringUtils.isBlank(submitOrderBO.getUserId()))
            return APIResponse.errorMsg("Missing User ID");

        if (StringUtils.isBlank(submitOrderBO.getItemSpecIds()))
            return APIResponse.errorMsg("Missing Item Spac ID(s)");

        Integer payType = submitOrderBO.getPayMethod();
        if (payType == null)
            return APIResponse.errorMsg("Missing User ID");
        else
            if (payType != PaymentType.Alipay.type && payType != PaymentType.WechatPay.type)
                return APIResponse.errorMsg("Unsupported Payment Type: " + payType);
        // Create Order
        // Update Shopping Card

        if (1 == 2)
            return APIResponse.errorMsg("I hate you");

        return APIResponse.ok(this.orderService.createOrder(submitOrderBO));
    }
}