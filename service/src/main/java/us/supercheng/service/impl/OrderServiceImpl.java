package us.supercheng.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.supercheng.bo.SubmitOrderBO;
import us.supercheng.enums.OrderStatusEnum;
import us.supercheng.enums.YesOrNo;
import us.supercheng.mapper.OrderItemsMapper;
import us.supercheng.mapper.OrderStatusMapper;
import us.supercheng.mapper.OrdersMapper;
import us.supercheng.pojo.*;
import us.supercheng.service.AddressService;
import us.supercheng.service.ItemsService;
import us.supercheng.service.OrderService;
import us.supercheng.vo.MerchantOrdersVO;
import us.supercheng.vo.OrderVO;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    String payReturnUrl = "http://api.z.mukewang.com/foodie-dev-api/orders/notifyMerchantOrderPaid";

    @Autowired
    private Sid sid;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemsService itemsService;

    @Transactional
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        Orders order = new Orders();
        List<String> list = new ArrayList<>();
        String[] specIds = submitOrderBO.getItemSpecIds().split(",");
        Date now = new Date();
        String orderId = this.sid.nextShort(),
               userId = "";
        int totalAmt = 0, actualAmt = 0, postAmt = 0;

        order.setId(orderId);
        order.setPayMethod(submitOrderBO.getPayMethod());
        userId = submitOrderBO.getUserId();
        order.setUserId(userId);

        if (!StringUtils.isBlank(submitOrderBO.getLeftMsg())) {
            order.setLeftMsg(submitOrderBO.getLeftMsg());
        }

        UserAddress address = this.addressService.getUserAddressByUserIdAndAddressId(userId, submitOrderBO.getAddressId());

        if (!StringUtils.isBlank(address.getProvince())) {
            list.add(address.getProvince());
        }

        if (!StringUtils.isBlank(address.getCity())) {
            list.add(address.getCity());
        }

        if (!StringUtils.isBlank(address.getDistrict())) {
            list.add(address.getDistrict());
        }

        if (!StringUtils.isBlank(address.getDetail())) {
            list.add(address.getDetail());
        }

        StringBuilder sb = new StringBuilder();
        for (int i=0, len=list.size(); i<len; i++)
            if (i == len - 1)
                sb.append(list.get(i));
            else
                sb.append(list.get(i) + ", ");

        if (sb.length() > 0)
            order.setReceiverAddress(sb.toString());

        if (!StringUtils.isBlank(address.getReceiver())) {
            order.setReceiverName(address.getReceiver());
        }

        if (!StringUtils.isBlank(address.getMobile())) {
            order.setReceiverMobile(address.getMobile());
        }


        OrderStatus orderStatus = new OrderStatus();


        for (String specId : specIds) {
            OrderItems orderItems = new OrderItems();

            ItemsSpec itemsSpec = this.itemsService.getItemsSpecByItemsSpecId(specId);

            if (itemsSpec != null) {
                Items items = this.itemsService.getItemsByItemsId(itemsSpec.getItemId());

                if (items != null) {
                    totalAmt += itemsSpec.getPriceNormal();
                    actualAmt += itemsSpec.getPriceDiscount();

                    orderItems.setBuyCounts(1);
                    orderItems.setId(this.sid.nextShort());
                    orderItems.setItemId(itemsSpec.getItemId());
                    ItemsImg img = this.itemsService.getItemsImgMainByItemId(items.getId());
                    if (img != null)
                        orderItems.setItemImg(img.getUrl());
                    orderItems.setItemName(items.getItemName());
                    orderItems.setItemSpecId(specId);
                    orderItems.setItemSpecName(itemsSpec.getName());
                    orderItems.setOrderId(orderId);
                    orderItems.setPrice(itemsSpec.getPriceDiscount());

                    this.orderItemsMapper.insert(orderItems);
                    Map<String, Object> map = new HashMap<>();
                    map.put("specId", specId);
                    map.put("amt", 1);
                    try {
                        this.itemsService.updateItemsSpecCount(map);
                    } catch (Exception ex) {
                        throw ex;
                    }
                }
            }
        }

        orderStatus.setCreatedTime(now);
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);

        this.orderStatusMapper.insert(orderStatus);

        // money
        order.setTotalAmount(totalAmt);
        order.setRealPayAmount(actualAmt);
        order.setPostAmount(postAmt);

        order.setIsComment(YesOrNo.No.type);
        order.setIsDelete(YesOrNo.No.type);

        order.setCreatedTime(now);
        order.setUpdatedTime(now);
        this.ordersMapper.insert(order);

        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setAmount(actualAmt + postAmt);
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setPayMethod(submitOrderBO.getPayMethod());
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        OrderVO ret = new OrderVO();
        ret.setOrders(order);
        ret.setMerchantOrdersVO(merchantOrdersVO);

        return ret;
    }

    @Transactional
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setOrderStatus(orderStatus);
        os.setPayTime(new Date());
        this.orderStatusMapper.updateByPrimaryKeySelective(os);
    }
}