package us.supercheng.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.Map;

public interface ItemsCommentsMapperCustom {
    void saveUserCommentList(@Param(("paraMap")) Map<String, Object> map);
}