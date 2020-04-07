package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import us.supercheng.bo.SubmitOrderBO;
import us.supercheng.enums.OrderStatusEnum;
import us.supercheng.enums.PaymentType;
import us.supercheng.pojo.OrderStatus;
import us.supercheng.service.ItemsService;
import us.supercheng.service.OrderService;
import us.supercheng.utils.APIResponse;
import us.supercheng.vo.MerchantOrdersVO;
import us.supercheng.vo.OrderVO;

@RestController
@RequestMapping("orders")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private RestTemplate restTemplate;

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

        OrderVO orderVO = this.orderService.createOrder(submitOrderBO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(orderVO.getMerchantOrdersVO(), headers);

        ResponseEntity<APIResponse> respEntity = restTemplate.postForEntity(PAYMENT_CENTER, entity, APIResponse.class);

        APIResponse resp = respEntity.getBody();
        if (resp == null || resp.getStatus() != 200) {
            return APIResponse.errorMsg("Interval Payment error......");
        }

        // Update Shopping Card

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