package us.supercheng.service;

import us.supercheng.pojo.Category;
import java.util.List;

public interface CategoryService {
    Integer CATEGORY_BASE_LEVEL = 1;

    List<Category> getCategoriesByType(Integer typeId);
    List<Category> getBaseCategories();
}