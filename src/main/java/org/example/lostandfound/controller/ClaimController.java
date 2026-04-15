package org.example.lostandfound.controller;


import jakarta.annotation.Resource;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.dto.ClaimDto;
import org.example.lostandfound.service.ClaimService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/claims")
@Validated
public class ClaimController {

    @Resource
    private ClaimService claimService;


    /*
    * 获取认领列表
    * 含有name,time,location,pathName
    * */
    @GetMapping("/{senderId}/{receiverId}/{c_id}/detail")
    private Result<List<Map<String,Object>>> getClaim(
            @PathVariable int senderId,
            @PathVariable int receiverId,
            @PathVariable int  c_id) {
        return Result.success(claimService.getClaim(senderId,receiverId,c_id));
    }

    /*
    * 获取特定物品，特定认领人和接受人的
    * */
    @GetMapping("/{senderId}/{receiverId}/{c_id}")
    public Result<ClaimDto> getClaimDetail(
            @PathVariable int senderId,
            @PathVariable int receiverId,
            @PathVariable int c_id) {
        ClaimDto claimDto=claimService.getClaimDto(senderId,receiverId,c_id);
        return Result.success(claimDto);
    }

    /*
    * 获取认领消息
    * */
    @GetMapping("/{senderId}/message")
    public Result<List<ClaimDto>> getClaimMessage(
            @PathVariable int senderId) {
        return Result.success(claimService.getClaimMessage(senderId));
    }

    /*
    * 获取我的认领列表
    * 含有sender
    * */
    @GetMapping("/{receiverId}")
    public Result<List<Map<String,Object>>> getMyClaim(
            @PathVariable int receiverId) {
        return Result.success(claimService.getMyClaim(receiverId));
    }

    /*
    * 提交认领
    * */
    @PostMapping("/{senderId}/{receiverId}/{c_id}")
    public Result<?> updateClaim(
            @PathVariable int senderId,
            @PathVariable int receiverId,
            @PathVariable int c_id,
            @RequestParam("reason") String reason) {
        claimService.addClaim(senderId,receiverId,c_id,reason);
        return Result.success("提交认领成功");
    }

    /*
    * 同意认领
    * */
    @PutMapping("/{id}/agree")
    public Result<?> agreeClaim(
            @PathVariable int id) {
        claimService.agreeClaim(id);
        return Result.success("同意认领成功");
    }

    /*
    * 拒绝认领
    * */
    @PutMapping("/{id}/reject")
    public Result<?> rejectClaim(
            @PathVariable int id) {
        claimService.rejectClaim(id);
        return Result.success("拒绝认领成功");
    }

    /*
    * 要求补充理由
    * */
    @PutMapping("/{id}/requireReason")
    public Result<?> requireReasonClaim(
            @PathVariable int id) {
        claimService.requireReasonClaim(id);
        return Result.success("要求补充理由成功");
    }



}
