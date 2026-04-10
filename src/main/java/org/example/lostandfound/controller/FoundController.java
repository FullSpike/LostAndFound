package org.example.lostandfound.controller;


import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.Found;
import org.example.lostandfound.pojo.Lost;
import org.example.lostandfound.service.FoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/founds")
@Validated
public class FoundController {

    @Autowired
    private FoundService foundService;

    @PostMapping()
    public Result<?> addFound(@RequestParam("found") String found,
                             @RequestParam("pathName") MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        Found foundObj=objectMapper.readValue(found, Found.class);


        foundService.addFound(foundObj,file);
        return Result.success("新增成功");

    }

    @GetMapping()
    public Result<?> getFoundList() {
        return Result.success(foundService.getFoundList());
    }

    @GetMapping("/{f_id}")
    public Result<?> getMyFoundList(@PathVariable("f_id") int f_id){
        return Result.success(foundService.getMyFoundList(f_id));
    }

}
