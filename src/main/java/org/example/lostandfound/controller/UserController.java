package org.example.lostandfound.controller;


import jakarta.annotation.Resource;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Resource
    public UserService userService;

    @PostMapping()
    public Result<?> register(@Validated @RequestBody User user){
        userService.register(user);
        return Result.success("注册成功");
    }

    @PutMapping("/{id}")
    public Result<?> updateUser(@PathVariable int id,
                                @Validated @RequestBody User user){
        userService.updateUser(id,user);
        return Result.success("更新成功");
    }

    @PostMapping("/{id}/password")
    public Result<?> checkPassword(@PathVariable int id,
                                   @RequestParam("password") String password){
        if (!userService.checkPassword(id,password)){
            return Result.error("密码错误","400");
        }
        return Result.success("密码正确");

    }

    @PutMapping("/{id}/password")
    public Result<?> updatePassword(@PathVariable int id,
                                   @RequestParam("password") String password){
        userService.updatePassword(id,password);
        return Result.success("密码更新成功");
    }

    @PutMapping("/{id}/avatar")
    public Result<?> updateAvatar(@PathVariable int id,
                                   @RequestParam("avatar") MultipartFile avatar){
        return Result.success(userService.updateAvatar(id,avatar));
    }

}
