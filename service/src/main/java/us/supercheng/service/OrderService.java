package us.supercheng.service;

import us.supercheng.bo.SubmitOrderBO;
import us.supercheng.vo.OrderVO;

public interface OrderService {
    OrderVO createOrder(SubmitOrderBO submitOrderBO);
    void updateOrderStatus(String orderId, Integer orderStatus);
}