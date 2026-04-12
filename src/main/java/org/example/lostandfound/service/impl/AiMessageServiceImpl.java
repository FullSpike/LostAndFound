package org.example.lostandfound.service.impl;

import org.example.lostandfound.mapper.AiMessageMapper;
import org.example.lostandfound.mapper.FoundMapper;
import org.example.lostandfound.mapper.LostMapper;
import org.example.lostandfound.pojo.AiMessage;
import org.example.lostandfound.pojo.Found;
import org.example.lostandfound.pojo.Lost;
import org.example.lostandfound.service.AiMessageService;
import org.example.lostandfound.utils.AiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AiMessageServiceImpl implements AiMessageService {

    @Autowired
    private AiMessageMapper aiMessageMapper;

    @Autowired
    private LostMapper lostMapper;

    @Autowired
    private FoundMapper foundMapper;


    @Override
    public List<AiMessage> getAiMessage(int a_id) {
        return aiMessageMapper.selectByAid(a_id);
    }

    @Override
    public String updateAiMessage(int a_id, String userMessage) {
        //先把用户的信息存进数据库
        AiMessage userMessageObj = new AiMessage();
        userMessageObj.setA_id(a_id);
        userMessageObj.setRole("user");
        userMessageObj.setContent(userMessage);
        userMessageObj.setTime(LocalDateTime.now().toString());
        aiMessageMapper.insert(userMessageObj);

        /*//获取丢失和拾取物的信息
        List<Lost> lostList = lostMapper.getAllLostList();
        List<Found> foundList = foundMapper.getFoundList();

        Map<String,Integer> lostLocationMap = lostListToLocationMap(lostList);
        Map<String,Integer> lostNameMap = lostListToNameMap(lostList);
        Map<String,Integer> foundLocationMap = foundListToLocationMap(foundList);
        Map<String,Integer> foundNameMap = foundListToNameMap(foundList);

        StringBuilder lostDescriptionBuilder = new StringBuilder();
        StringBuilder foundDescriptionBuilder = new StringBuilder();

        lostLocationMap.forEach((k,v) ->{
            String str="在"+k+"有"+v+"个失物";
            lostDescriptionBuilder.append(str);
            lostDescriptionBuilder.append("\n");
        });
        lostNameMap.forEach((k,v) ->{
            String str="有"+v+"个失物叫做"+k;
            lostDescriptionBuilder.append(str);
            lostDescriptionBuilder.append("\n");
        });
        foundLocationMap.forEach((k,v) ->{
            String str="在"+k+"有"+v+"个拾取物";
            foundDescriptionBuilder.append(str);
            foundDescriptionBuilder.append("\n");
        });
        foundNameMap.forEach((k,v) ->{
            String str="有"+v+"个拾取物叫做"+k;
            foundDescriptionBuilder.append(str);
            foundDescriptionBuilder.append("\n");
        });*/

        List<Lost> lostList = lostMapper.getAllLostList();
        List<Found> foundList = foundMapper.getFoundList();
        String context = "有以下失物信息（lost（）就是一个失物，里面的数据就是一个失物的信息）"+
                lostList.toString()+"\n"+
                "有以下拾取物信息（found（）就是一个拾取物，里面的数据就是一个拾取物的信息）"+
                foundList.toString();


        //调用aiutil获取ai的信息
        String description="你是一个失物和拾取物招领的ai助手，有以下已知条件："+
                context+
                "请回答问题"+userMessage+
                "在30字以内，回答不需要解析";
        String aiMessage = AiUtil.getAiResponse(description);

        //把ai的信息存进数据库
        AiMessage aiMessageObj = new AiMessage();
        aiMessageObj.setA_id(a_id);
        aiMessageObj.setRole("ai");
        aiMessageObj.setContent(aiMessage);
        aiMessageObj.setTime(LocalDateTime.now().toString());
        aiMessageMapper.insert(aiMessageObj);

        return aiMessage;

    }

    private Map<String, Integer> foundListToNameMap(List<Found> foundList) {
        Map<String, Integer> nameMap = new HashMap<>();
        for (Found found : foundList) {
            String name = found.getName();
            if (nameMap.containsKey(name)) {
                nameMap.put(name, nameMap.get(name) + 1);
            } else {
                nameMap.put(name, 1);
            }
        }
        return nameMap;
    }

    private Map<String, Integer> foundListToLocationMap(List<Found> foundList) {
        Map<String, Integer> locationMap = new HashMap<>();
        for (Found found : foundList) {
            String location = found.getLocation();
            if (locationMap.containsKey(location)) {
                locationMap.put(location, locationMap.get(location) + 1);
            } else {
                locationMap.put(location, 1);
            }
        }
        return locationMap;
    }

    private Map<String, Integer> lostListToNameMap(List<Lost> lostList) {
        Map<String, Integer> nameMap = new HashMap<>();
        for (Lost lost : lostList) {
            String name = lost.getName();
            if (nameMap.containsKey(name)) {
                nameMap.put(name, nameMap.get(name) + 1);
            } else {
                nameMap.put(name, 1);
            }
        }
        return nameMap;
    }

    private Map<String, Integer> lostListToLocationMap(List<Lost> lostList) {
        Map<String, Integer> locationMap = new HashMap<>();
        for (Lost lost : lostList) {
            String location = lost.getLocation();
            if (locationMap.containsKey(location)) {
                locationMap.put(location, locationMap.get(location) + 1);
            } else {
                locationMap.put(location, 1);
            }
        }
        return locationMap;
    }
}
