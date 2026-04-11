package org.example.lostandfound.service;

import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {
    List<Message> getMessage(int userId1, int userId2);

    void sendMessage(int senderId, int receiverId, Message message);

    List<Map<String, Object>> getMessageList(int receiverId);

    void refreshUnread(int receiverId, int senderId);
}
