package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.lostandfound.pojo.Found;

import java.util.List;

public interface FoundMapper {
    void addFound(@Param("found") Found found,
                  @Param("pathName") String pathName);

    @Select("select * from found")
    List<Found> getFoundList();

    @Select("select * from found where f_id=#{f_id}")
    List<Found> getMyFoundList(@Param("f_id") int f_id);

    @Update("update found set note=#{note} where id=#{id}")
    void updateNote( @Param("id") int id, @Param("note") String note);

    @Update("update found set is_report=#{is_report}, report_reason=#{report_reason} where id=#{id}")
    void updateReport( @Param("id") int id,
                       @Param("is_report") String is_report,
                       @Param("report_reason") String report_reason);

    @Select("select * from found where id=#{id}")
    Found selectById(@Param("id") int id);

    @Delete("delete from found where id=#{id}")
    void deleteFound( @Param("id") int id);

    void updateFound( @Param("id") int id, @Param("found") Found found);
}
