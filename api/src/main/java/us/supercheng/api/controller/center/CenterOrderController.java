package us.supercheng.api.controller.center;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.supercheng.api.controller.BaseController;
import us.supercheng.service.center.CenterOrderService;
import us.supercheng.utils.APIResponse;

@RestController
@RequestMapping("myorders")
public class CenterOrderController extends BaseController {

    @Autowired
    private CenterOrderService centerOrderService;

    @PostMapping("query")
    public APIResponse query(@RequestParam String userId, @RequestParam(required = false, name = "orderStatus") Integer orderStatus,
                             @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(userId))
            return APIResponse.errorMsg("Missing required field User ID");

        if (page == null)
            page = DEFAULT_PAGE;

        if (pageSize == null)
            pageSize = DEFAULT_PAGE_SIZE;

        return APIResponse.ok(this.centerOrderService.query(userId, orderStatus, page, pageSize));
    }
}
