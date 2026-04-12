package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.lostandfound.pojo.AiMessage;

import java.util.List;

public interface AiMessageMapper {

    List<AiMessage> selectByAid(int a_id);

    @Insert("insert into aimessage (a_id, role, content, time) values " +
            "(#{aiMessage.a_id}, #{aiMessage.role}, #{aiMessage.content}, #{aiMessage.time})")
    void insert(@Param("aiMessage") AiMessage aiMessage);
}
