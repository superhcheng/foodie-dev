package us.supercheng.service.center.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.supercheng.mapper.OrdersMapperCustom;
import us.supercheng.service.center.CenterOrderService;
import us.supercheng.utils.PagedResult;
import us.supercheng.vo.MyOrdersVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CenterOrderServiceImpl implements CenterOrderService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

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
}
