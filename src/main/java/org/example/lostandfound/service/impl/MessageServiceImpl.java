package org.example.lostandfound.service.impl;


import jakarta.annotation.Resource;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.MessageMapper;
import org.example.lostandfound.mapper.UserMapper;
import org.example.lostandfound.pojo.Message;
import org.example.lostandfound.service.MessageService;
import org.example.lostandfound.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<Message> getMessage(int userId1, int userId2) {
        if(userMapper.selectById(userId1) == null || userMapper.selectById(userId2) == null){
            throw new ServiceException("用户不存在", "404");
        }
        return messageMapper.selectByUserId1AndUserId2(userId1, userId2);
    }

    @Override
    public void sendMessage(int senderId, int receiverId, Message message) {
        message.setTime(LocalDateTime.now().toString());
        message.setIs_read("否");
        messageMapper.insert(message);
    }

    @Override
    public List<Map<String, Object>> getMessageList(int receiverId) {
        if(userMapper.selectById(receiverId) == null){
            throw new ServiceException("用户不存在", "404");
        }
        return messageMapper.selectByReceiverId(receiverId);
    }

    @Override
    public void refreshUnread(int receiverId, int senderId) {
        if(userMapper.selectById(receiverId) == null || userMapper.selectById(senderId) == null){
            throw new ServiceException("用户不存在", "404");
        }
        messageMapper.updateIsRead(senderId, receiverId, "是");
    }
}
