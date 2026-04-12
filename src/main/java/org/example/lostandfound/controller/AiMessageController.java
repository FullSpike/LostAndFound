package org.example.lostandfound.controller;


import jakarta.annotation.Resource;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.AiMessage;
import org.example.lostandfound.service.AiMessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/aiMessages")
@Validated
public class AiMessageController {


    @Resource
    private AiMessageService aiMessageService;

    @GetMapping("/{a_id}")
    public Result<List<AiMessage>> getAiMessage(@PathVariable int a_id) {
        return Result.success(aiMessageService.getAiMessage(a_id));
    }

    @PostMapping("/{a_id}")
    public Result<String> updateAiMessage(@PathVariable int a_id, @RequestParam("userMessage") String userMessage) {
        return Result.success(aiMessageService.updateAiMessage(a_id,userMessage));
    }
}
