package org.example.lostandfound.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.lostandfound.pojo.Claim;

import java.util.List;
import java.util.Map;

public interface ClaimMapper {


    List<Map<String, Object>> selectByUserIdCid(@Param("senderId") int senderId,
                                          @Param("receiverId") int receiverId,
                                          @Param("c_id") int c_id);

    List<Map<String, Object>> selectByReceiverId(@Param("receiverId") int receiverId);

    void insert(@Param("claim") Claim claim);

    void updateById(@Param("claim") Claim claim);

    @Select("select * from claim where id=#{id}")
    Claim selectById(@Param("id") int id);

    List<Claim> selectByReceiverIdSenderId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    @Select("select * from claim where senderId=#{senderId}")
    List<Claim> selectBySenderId(int senderId);

    @Select("select * from claim where senderId=#{senderId} and receiverId=#{receiverId} and c_id=#{c_id}")
    List<Claim> selectBySenderIdReceiveridC_id( @Param("senderId") int senderId,
                                                @Param("receiverId") int receiverId,
                                                @Param("c_id") int c_id);
}
