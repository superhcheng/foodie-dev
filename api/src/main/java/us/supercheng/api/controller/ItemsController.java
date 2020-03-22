package us.supercheng.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.supercheng.pojo.Items;
import us.supercheng.pojo.ItemsImg;
import us.supercheng.pojo.ItemsParam;
import us.supercheng.pojo.ItemsSpec;
import us.supercheng.service.ItemsService;
import us.supercheng.utils.APIResponse;
import us.supercheng.vo.ItemsInfoVO;
import java.util.List;

@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;


    @GetMapping("info/{id}")
    public APIResponse getItemDetailById(@PathVariable String id) {
        if (id == null)
            return APIResponse.errorMsg("Missing Item ID in request");

        Items item = this.itemsService.queryItemById(id);
        List<ItemsImg> imgs = this.itemsService.queryItemImgListByItemId(id);
        List<ItemsSpec> specs = this.itemsService.queryItemSpacListByItemId(id);
        ItemsParam param = this.itemsService.queryItemParamByItemId(id);

        ItemsInfoVO itemsInfoVO = new ItemsInfoVO();
        itemsInfoVO.setItem(item);
        itemsInfoVO.setItemImgList(imgs);
        itemsInfoVO.setItemSpecList(specs);
        itemsInfoVO.setItemParams(param);

        return APIResponse.ok(itemsInfoVO);
    }
}