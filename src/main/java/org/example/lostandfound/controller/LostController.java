package org.example.lostandfound.controller;


import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.Lost;
import org.example.lostandfound.service.LostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;


@CrossOrigin
@RestController
@RequestMapping("/losts")
@Validated
public class LostController {

    @Autowired
    private LostService lostService;

    @PostMapping()
    public Result<?> addLost(@RequestParam("lost") String lost,
                             @RequestParam("pathName")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        Lost lostObj=objectMapper.readValue(lost, Lost.class);


        lostService.addLost(lostObj,file);
        return Result.success("新增成功");

    }



}
