package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.*;
import org.example.lostandfound.pojo.Message;

import java.util.List;
import java.util.Map;

public interface MessageMapper {


       List<Message> selectByUserId1AndUserId2(@Param("userId1") int userId1,
                                            @Param("userId2") int userId2);


       @Insert("insert into message (senderId, receiverId, content, time, is_read) " +
               "values (#{message.senderId}, #{message.receiverId}, #{message.content}, #{message.time}, #{message.is_read})")
       void insert(@Param("message") Message message);

       List<Map<String, Object>> selectByReceiverId(@Param("receiverId") int receiverId);

       @Update("update message set is_read = #{is_read} where senderId = #{senderId} and receiverId = #{receiverId}")
       void updateIsRead( @Param("senderId") int senderId,
                          @Param("receiverId") int receiverId,
                          @Param("is_read") String is_read);
}
