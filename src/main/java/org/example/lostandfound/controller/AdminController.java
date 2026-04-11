package org.example.lostandfound.controller;


import jakarta.annotation.Resource;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.pojo.Admin;
import org.example.lostandfound.service.AdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
