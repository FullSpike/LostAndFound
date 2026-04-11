package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.lostandfound.pojo.Admin;

public interface AdminMapper {
    Admin selectByEmail(@Param("email") String email);

    Admin selectByPhone(@Param("phone") String phone);

    void insert(@Param("admin") Admin admin);
}
