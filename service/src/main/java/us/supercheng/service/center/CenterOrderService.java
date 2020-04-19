package us.supercheng.service.center;

import us.supercheng.utils.PagedResult;

public interface CenterOrderService {
    PagedResult query(String userId, Integer orderStatus, Integer pageNum, Integer pageSize);
}
