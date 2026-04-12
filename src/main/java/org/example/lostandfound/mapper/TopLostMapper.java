package org.example.lostandfound.mapper;


import org.apache.ibatis.annotations.Param;
import org.example.lostandfound.pojo.Lost;

public interface TopLostMapper {



    void toTopLost(@Param("id") int id,@Param("lost") Lost lost);
}
