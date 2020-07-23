package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import us.supercheng.bo.SubmitOrderBO;
import us.supercheng.enums.OrderStatusEnum;
import us.supercheng.enums.PaymentType;
import us.supercheng.pojo.OrderStatus;
import us.supercheng.service.OrderService;
import us.supercheng.utils.APIResponse;
import us.supercheng.vo.OrderVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("orders")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @PostMapping("create")
    public APIResponse createOrder(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest req, HttpServletResponse resp) {
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
        OrderVO orderVO = this.orderService.createOrder(submitOrderBO, PAYMENT_CENTER, req, resp);
        return APIResponse.ok(orderVO.getOrders().getId());
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer markPaidOrder(@RequestParam String merchantOrderId) {
        if (StringUtils.isBlank(merchantOrderId))
            return HttpStatus.BAD_REQUEST.value();

        this.orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public APIResponse getPaidOrderInfo(@RequestParam String orderId) {
        if (StringUtils.isBlank(orderId))
            return APIResponse.errorMsg("Missing required argument Order Id");

        OrderStatus orderStatus = this.orderService.getOrderStatusByOrderId(orderId);

        if (orderStatus == null)
            return APIResponse.errorMsg("No such Order Id");

        return APIResponse.ok(orderStatus);
    }
}