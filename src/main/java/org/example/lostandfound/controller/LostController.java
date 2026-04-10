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

    @GetMapping()
    public Result<?> getLostList(){

        return Result.success(lostService.getLostList());
    }

    @GetMapping("/top")
    public Result<?> getTopLostList(){

        return Result.success(lostService.getTopLostList());
    }


    @GetMapping("/{l_id}")
    public Result<?> getMyLostList(@PathVariable("l_id") int l_id){
        return Result.success(lostService.getMyLostList(l_id));
    }

    /*
    * 举报物品
    */
    @PutMapping("/{id}/report")
    public Result<?> reportLost(@PathVariable("id") int id,
                                @RequestParam("report_reason") String report_reason){
        lostService.reportLost(id,report_reason);
        return Result.success("举报成功");
    }

    /*
    * 留言物品
    * */
    @PutMapping("/{id}/note")
    public Result<?> noteLost(@PathVariable("id") int id,
                                @RequestParam("note") String note){
        lostService.noteLost(id,note);
        return Result.success("留言成功");
    }




}
