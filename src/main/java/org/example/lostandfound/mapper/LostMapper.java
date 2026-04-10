package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.lostandfound.pojo.Lost;

import java.util.List;

public interface LostMapper {
    void addLost(@Param("lost") Lost lost,@Param("pathName") String pathName);

    @Select("select * from lost")
       List<Lost> getLostList();

    @Select("select * from toplost")
    List<Lost> getTopLostList();

    @Select("select * from lost where l_id=#{l_id}")
    List<Lost> getMyLostList(@Param("l_id") int l_id);

    void reportLost(@Param("id") int id,@Param("is_report") String is_report,
                    @Param("report_reason") String report_reason);

    @Update("update lost set note=#{note} where id=#{id}")
    void noteLost(@Param("id") int id, @Param("note") String note);
}
