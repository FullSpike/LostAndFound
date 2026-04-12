package org.example.lostandfound.service;

import org.example.lostandfound.pojo.AiMessage;

import java.util.List;

public interface AiMessageService {
    List<AiMessage> getAiMessage(int a_id);

    String updateAiMessage(int a_id, String userMessage);
}
