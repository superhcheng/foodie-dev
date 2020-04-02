package us.supercheng.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.supercheng.bo.SubmitOrderBO;
import us.supercheng.enums.YesOrNo;
import us.supercheng.mapper.OrdersMapper;
import us.supercheng.pojo.Orders;
import us.supercheng.pojo.UserAddress;
import us.supercheng.service.AddressService;
import us.supercheng.service.OrderService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Sid sid;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean createOrder(SubmitOrderBO submitOrderBO) {
        Orders order = new Orders();
        List<String> list = new ArrayList<>();

        order.setId(this.sid.nextShort());
        order.setPayMethod(submitOrderBO.getPayMethod());
        order.setUserId(submitOrderBO.getUserId());

        if (!StringUtils.isBlank(submitOrderBO.getLeftMsg())) {
            order.setLeftMsg(submitOrderBO.getLeftMsg());
        }

        UserAddress address = this.addressService.getUserAddressByUserIdAndAddressId(submitOrderBO.getUserId(),
                submitOrderBO.getAddressId());

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

        // money
        order.setTotalAmount(0);
        order.setRealPayAmount(0);
        order.setPostAmount(0);

        order.setIsComment(YesOrNo.No.type);
        order.setIsDelete(YesOrNo.No.type);

        Date now = new Date();
        order.setCreatedTime(now);
        order.setUpdatedTime(now);

        try {
            return this.ordersMapper.insert(order) == 1;
        } catch (Exception ex) {
            return false;
        }
    }
}