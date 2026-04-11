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

    /*
     * 留言物品
     * */
    @PutMapping("/{id}/note")
    public Result<?> noteLost(@PathVariable("id") int id,
                              @RequestParam("note") String note){
        foundService.noteFound(id,note);
        return Result.success("留言成功");
    }

    /*
     * 举报物品
     */
    @PutMapping("/{id}/report")
    public Result<?> reportLost(@PathVariable("id") int id,
                                @RequestParam("report_reason") String report_reason){
        foundService.reportFound(id,report_reason);
        return Result.success("举报成功");
    }

    /*
     * 删除物品
     * */
    @DeleteMapping("/{id}")
    public Result<?> deleteFound(@PathVariable("id") int id) {
        foundService.deleteFound(id);
        return Result.success("删除成功");
    }

    /*
     * 修改物品
     * */
    @PutMapping("/{id}")
    public Result<?> updateLost(@PathVariable("id") int id,
                                @RequestBody Found found){
        foundService.updateFound(id,found);
        return Result.success("修改成功");
    }
}
