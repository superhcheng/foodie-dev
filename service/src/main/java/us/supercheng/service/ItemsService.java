package us.supercheng.service;

import us.supercheng.pojo.Items;
import us.supercheng.pojo.ItemsImg;
import us.supercheng.pojo.ItemsParam;
import us.supercheng.pojo.ItemsSpec;
import java.util.List;

public interface ItemsService {
    Items queryItemById(String id);
    List<ItemsImg> queryItemImgListByItemId(String id);
    List<ItemsSpec> queryItemSpacListByItemId(String id);
    ItemsParam queryItemParamByItemId(String id);
}