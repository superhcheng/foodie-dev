package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.mapper.CategoryMapper;
import us.supercheng.mapper.CategoryMapperCustom;
import us.supercheng.pojo.Category;
import us.supercheng.service.CategoryService;
import us.supercheng.vo.CategoryVO;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> getCategoriesByType(Integer typeId) {
        Example categoryExp = new Example(Category.class);
        Example.Criteria categoryCriteria = categoryExp.createCriteria();
        categoryCriteria.andEqualTo("type", typeId);
        return this.categoryMapper.selectByExample(categoryExp);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> getBaseCategories() {
        return this.getCategoriesByType(CategoryService.CATEGORY_BASE_LEVEL);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatListByParentId(Integer parentId) {
        return this.categoryMapperCustom.getSubCatList(parentId);
    }
}