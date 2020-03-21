package us.supercheng.mapper;

import us.supercheng.mymapper.MyMapper;
import us.supercheng.pojo.Category;
import us.supercheng.vo.CategoryVO;
import java.util.List;

public interface CategoryMapperCustom extends MyMapper<Category> {
    List<CategoryVO> getSubCatList(Integer parentId);
}