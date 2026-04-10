package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.lostandfound.pojo.Found;

import java.util.List;

public interface FoundMapper {
    void addFound(@Param("found") Found found,
                  @Param("pathName") String pathName);

    @Select("select * from found")
    List<Found> getFoundList();

    @Select("select * from found where f_id=#{f_id}")
    List<Found> getMyFoundList(@Param("f_id") int f_id);
}
