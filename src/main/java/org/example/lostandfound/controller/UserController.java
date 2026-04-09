package org.example.lostandfound.controller;


import jakarta.annotation.Resource;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Resource
    public UserService userService;

    @PutMapping("/{id}")
    public Result<?> updateUser(@PathVariable int id,@Validated @RequestBody User user){
        userService.updateUser(id,user);
        return Result.success("更新成功");
    }

}
