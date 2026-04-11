package org.example.lostandfound.controller;

import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.Message;
import org.example.lostandfound.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/messages")
@Validated
public class MessageController {

    @Resource
    private MessageService messageService;

    @GetMapping("/{userId1}/{userId2}")
    public Result<?> getMessage(@PathVariable int userId1,
                                      @PathVariable int userId2) {
        return Result.success(messageService.getMessage(userId1, userId2));
    }

    @PostMapping("/{senderId}/{receiverId}")
    public Result<?> sendMessage(@PathVariable int senderId,
                                  @PathVariable int receiverId,
                                  @RequestBody Message message) {
        messageService.sendMessage(senderId, receiverId, message);
        return Result.success("发送成功");
    }

    @GetMapping("/{receiverId}")
    public Result<List<Map<String, Object>>> getMessageList(@PathVariable int receiverId) {
        return Result.success(messageService.getMessageList(receiverId));
    }


    /*
    * 刷新对话未读消息
    * */
    @PutMapping("/{receiverId}/{senderId}")
    public Result<?> refreshUnread(@PathVariable int receiverId,
                                   @PathVariable int senderId) {
        messageService.refreshUnread(receiverId, senderId);
        return Result.success("刷新成功");
    }
}
