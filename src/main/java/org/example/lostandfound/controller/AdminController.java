package org.example.lostandfound.controller;


import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.Admin;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.service.AdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/admins")
@Validated
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping()
    public Result<?> register(@RequestBody Admin admin) {
        adminService.register(admin);
        return Result.success("注册成功");
    }

    @PutMapping("/{id}")
    public Result<?> updateUser(@PathVariable int id,
                                @Validated @RequestBody Admin admin){
        adminService.updateAdmin(id,admin);
        return Result.success("更新成功");
    }

    @PutMapping("/{id}/avatar")
    public Result<?> updateAvatar(@PathVariable int id,
                                  @RequestParam("avatar") MultipartFile avatar){
        return Result.success(adminService.updateAvatar(id,avatar));
    }

    @PostMapping("/{id}/password")
    public Result<?> checkPassword(@PathVariable int id,
                                   @RequestParam("password") String password){
        if (!adminService.checkPassword(id,password)){
            return Result.error("密码错误","400");
        }
        return Result.success("密码正确");

    }

    @PutMapping("/{id}/password")
    public Result<?> updatePassword(@PathVariable int id,
                                    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$") String password){
        adminService.updatePassword(id,password);
        return Result.success("密码更新成功");
    }
}
