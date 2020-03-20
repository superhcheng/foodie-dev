package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.mapper.CategoryMapper;
import us.supercheng.pojo.Category;
import us.supercheng.service.CategoryService;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoriesByType(Integer typeId) {
        Example categoryExp = new Example(Category.class);
        Example.Criteria categoryCriteria = categoryExp.createCriteria();
        categoryCriteria.andEqualTo("type", typeId);
        return this.categoryMapper.selectByExample(categoryExp);
    }

    @Override
    public List<Category> getBaseCategories() {
        return this.getCategoriesByType(CategoryService.CATEGORY_BASE_LEVEL);
    }
}
