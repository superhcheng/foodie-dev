package us.supercheng.service;

import us.supercheng.bo.SubmitOrderBO;

public interface OrderService {
    boolean createOrder(SubmitOrderBO submitOrderBO);
}