package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.lostandfound.pojo.User;

public interface UserMapper {


    User selectByUsername(@Param("username") String username);


    User selectByEmail(@Param("email") String email);

    User selectByPhone(@Param("phone") String phone);
}
