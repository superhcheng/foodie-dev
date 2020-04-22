package us.supercheng.service.center;

import us.supercheng.pojo.OrderItems;
import us.supercheng.pojo.Orders;
import us.supercheng.utils.PagedResult;

import java.util.List;

public interface CenterOrderService {
    PagedResult query(String userId, Integer orderStatus, Integer pageNum, Integer pageSize);
    boolean updateOrderStatus(String orderId, Integer fromOrderStatus, Integer toOrderStatus);
    boolean markOrderDelete(String orderId, String userId);
    Orders getOrdersByUserIdAndOrderId(String orderId, String userId);
    List<OrderItems> getOrderItemsByOrderId(String orderId);
    List<OrderItems> getReadyToCommentOrderItems(String userId, Integer pageNum, Integer pageSize);
}
