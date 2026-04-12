package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.lostandfound.pojo.Admin;

public interface AdminMapper {
    Admin selectByEmail(@Param("email") String email);

    Admin selectByPhone(@Param("phone") String phone);

    void insert(@Param("admin") Admin admin);

    @Select("select * from admin where id = #{id}")
    Admin selectById(@Param("id") int id);

    void update(@Param("id") int id, @Param("admin") Admin admin);

    @Update("update admin set avatar=#{avatar} where id=#{id}")
    void updateAvatar( @Param("id") int id, @Param("avatar") String avatar);
}
