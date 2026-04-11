package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.lostandfound.pojo.User;

import java.util.List;

public interface UserMapper {


    User selectByUsername(@Param("username") String username);


    User selectByEmail(@Param("email") String email);

    User selectByPhone(@Param("phone") String phone);

    void update(@Param("id") int id, @Param("user") User user);

    @Select("select * from user where id = #{id}")
    User selectById(@Param("id") int id);

    @Update("update user set avatar=#{avatar} where id=#{id}")
    void updateAvatar( @Param("id") int id, @Param("avatar") String avatar);

    void insert(@Param("user") User user);

    @Select("select * from user where id != #{id}")
    List<User> selectChatUsers( @Param("id") int id);
}
