package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.mapper.ItemsImgMapper;
import us.supercheng.mapper.ItemsMapper;
import us.supercheng.mapper.ItemsParamMapper;
import us.supercheng.mapper.ItemsSpecMapper;
import us.supercheng.mymapper.MyMapper;
import us.supercheng.pojo.*;
import us.supercheng.service.ItemsService;
import java.util.List;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    ItemsMapper itemsMapper;

    @Autowired
    ItemsImgMapper itemsImgMapper;

    @Autowired
    ItemsSpecMapper itemsSpecMapper;

    @Autowired
    ItemsParamMapper itemsParamMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String id) {
        return this.queryOneHelper(Items.class, this.itemsMapper, "id", id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgListByItemId(String id) {
        return this.queryManyHelper(ItemsImg.class, this.itemsImgMapper, "itemId", id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpacListByItemId(String id) {
        return this.queryManyHelper(ItemsSpec.class, this.itemsSpecMapper, "itemId", id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParamByItemId(String id) {
        return this.queryOneHelper(ItemsParam.class, this.itemsParamMapper, "itemId", id);
    }

    private <T> T queryOneHelper(Class clz, MyMapper mapper, String col, String val) {
        Example exp = new Example(clz);
        Example.Criteria criteria = exp.createCriteria();
        criteria.andEqualTo(col, val);
        return (T) mapper.selectOneByExample(exp);
    }

    private <T> List<T> queryManyHelper(Class clz, MyMapper mapper, String col, String val) {
        Example exp = new Example(clz);
        Example.Criteria criteria = exp.createCriteria();
        criteria.andEqualTo(col, val);
        return (List<T>) mapper.selectByExample(exp);
    }
}