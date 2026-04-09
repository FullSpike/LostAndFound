package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.lostandfound.pojo.Lost;

public interface LostMapper {
    void addLost(@Param("lost") Lost lost,@Param("pathName") String pathName);
}
