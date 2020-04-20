package us.supercheng.service.center.impl;

import com.github.pagehelper.PageInfo;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.enums.OrderStatusEnum;
import us.supercheng.enums.YesOrNo;
import us.supercheng.mapper.OrderStatusMapper;
import us.supercheng.mapper.OrdersMapper;
import us.supercheng.mapper.OrdersMapperCustom;
import us.supercheng.pojo.ItemsComments;
import us.supercheng.pojo.OrderStatus;
import us.supercheng.pojo.Orders;
import us.supercheng.service.center.CenterOrderService;
import us.supercheng.utils.PagedResult;
import us.supercheng.vo.MyOrdersVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CenterOrderServiceImpl implements CenterOrderService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult query(String userId, Integer orderStatus, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();

        map.put("userId", userId);

        if (orderStatus != null)
            map.put("orderStatus", orderStatus);

        List<MyOrdersVO> myOrdersVOs = this.ordersMapperCustom.query(map);
        PageInfo<?> pageInfo = new PageInfo<>(myOrdersVOs);

        return new PagedResult(pageNum, pageInfo.getPages(), pageInfo.getTotal(), myOrdersVOs);
    }

    @Transactional
    @Override
    public boolean updateOrderStatus(String orderId, Integer fromOrderStatus, Integer toOrderStatus) {
        OrderStatus os = new OrderStatus();
        os.setOrderStatus(toOrderStatus);
        os.setDeliverTime(new Date());

        if (toOrderStatus == OrderStatusEnum.SUCCESS.type)
            os.setSuccessTime(new Date());

        Example exp = new Example(OrderStatus.class);
        Example.Criteria criteria = exp.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", fromOrderStatus);

        return orderStatusMapper.updateByExampleSelective(os, exp) > 0;
    }

    @Transactional
    @Override
    public boolean markOrderDelete(String orderId, String userId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setUserId(userId);
        order.setIsDelete(YesOrNo.Yes.type);
        order.setUpdatedTime(new Date());
        return this.ordersMapper.updateByPrimaryKeySelective(order) > 0;
    }

    @Override
    public Orders getOrdersByUserIdAndOrderId(String orderId, String userId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setUserId(userId);
        order.setIsDelete(YesOrNo.No.type);
        return this.ordersMapper.selectOne(order);
    }
}
