package org.example.lostandfound.service.impl;


import org.example.lostandfound.dto.ClaimDto;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.ClaimMapper;
import org.example.lostandfound.mapper.FoundMapper;
import org.example.lostandfound.mapper.UserMapper;
import org.example.lostandfound.pojo.Found;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.pojo.Claim;
import org.example.lostandfound.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimMapper claimMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FoundMapper foundMapper;



    @Override
    public List<Map<String, Object>> getClaim(int senderId,
                                              int receiverId,
                                              int c_id) {
        return claimMapper.selectByUserIdCid(senderId,receiverId,c_id);
    }

    @Override
    public List<Map<String, Object>> getMyClaim(int receiverId) {
        if(userMapper.selectById(receiverId) == null){
            throw new ServiceException("用户不存在","404");
        }
        return claimMapper.selectByReceiverId(receiverId);
    }

    @Override
    public void addClaim(int senderId, int receiverId, int c_id, String reason) {
        User sender = userMapper.selectById(senderId);
        User receiver = userMapper.selectById(receiverId);
        if(sender == null || receiver == null){
            throw new ServiceException("用户不存在","404");
        }
        Claim claim=new Claim();
        claim.setSenderId(senderId);
        claim.setReceiverId(receiverId);
        claim.setSender(sender.getUsername());
        claim.setReceiver(receiver.getUsername());
        claim.setReason(reason);
        claim.setC_id(c_id);
        claimMapper.insert(claim);
    }

    @Override
    public void agreeClaim(int id) {
        Claim claim = claimMapper.selectById(id);
        if(claim == null){
            throw new ServiceException("认领不存在","404");
        }
        claim.setIs_check("是");
        claimMapper.updateById(claim);
    }

    @Override
    public void rejectClaim(int id) {
        Claim claim = claimMapper.selectById(id);
        if(claim == null){
            throw new ServiceException("认领不存在","404");
        }
        claim.setIs_check("否");
        claimMapper.updateById(claim);
    }

    @Override
    public void requireReasonClaim(int id) {
        Claim claim = claimMapper.selectById(id);
        if(claim == null){
            throw new ServiceException("认领不存在","404");
        }
        claim.setIs_check("补");
        claimMapper.updateById(claim);
    }

    @Override
    public List<ClaimDto> getClaimMessage(int senderId) {

        /*List<ClaimDto> claimDtos = new ArrayList<>();

        List<User> users = userMapper.selectAll();
        for (User user : users) {
            if(user.getId() != senderId) {
                List<Claim> claims = claimMapper.selectByReceiverIdSenderId(senderId,user.getId());
                String check="";
                int c_id=0;
                if (claims == null) {
                    continue;
                }
                for (Claim claim : claims) {
                    c_id=claim.getC_id();
                    if(claim.getIs_check().equals("补")){
                        check="补";
                    }
                    if(claim.getIs_check().equals("是")){
                        check="是";
                        break;
                    }
                    if(claim.getIs_check().equals("否")){
                        check="否";
                        break;
                    }
                }
                claimDtos.add(ClaimDto.convertClaim(foundMapper.selectById(c_id).getName(),
                        new Claim(check,userMapper.selectById(user.getId()).getUsername())));
            }
        }
        return claimDtos;*/
        List<ClaimDto> claimDtos = new ArrayList<>();

        List<Claim> allClaims = claimMapper.selectBySenderId(senderId);

        //对claims根据receiverId分组
        Map<Integer, List<Claim>> claimsByReceiver = allClaims.stream()
                .collect(Collectors.groupingBy(s->s.getReceiverId()));

        for (Map.Entry<Integer, List<Claim>> entry : claimsByReceiver.entrySet()) {
            int receiverId = entry.getKey();
            List<Claim> claims = entry.getValue();

            User receiver = userMapper.selectById(receiverId);
            if (receiver == null) continue;

            //对claims根据c_id分组
            Map<Integer, List<Claim>> claimsByCid = claims.stream()
                    .collect(Collectors.groupingBy(s->s.getC_id()));

            for (Map.Entry<Integer, List<Claim>> entry1 : claimsByCid.entrySet()) {
                int c_id=entry1.getKey();
                List<Claim> claims1 = entry1.getValue();

                Found foundItem = foundMapper.selectById(c_id);
                if (foundItem == null) continue;

                String check=null;

                for (Claim claim : claims1) {
                    if("补".equals(claim.getIs_check())){
                        check="补";
                    }
                    if("是".equals(claim.getIs_check())){
                        check="是";
                        break;
                    }
                    if("否".equals(claim.getIs_check())){
                        check="否";
                        break;
                    }
                }

                String itemName = foundItem.getName();

                claimDtos.add(ClaimDto.convertClaim(itemName,
                        new Claim(check, receiver.getUsername())));
            }


        }
        return claimDtos;
    }

    @Override
    public ClaimDto getClaimDto(int senderId, int receiverId, int cId) {
        List<Claim> claims=claimMapper.selectBySenderIdReceiveridC_id(senderId,receiverId,cId);
        if(claims == null){
            return new ClaimDto();
        }
        //将所有的claims中集中到一个check中即
        String check=null;
        for (Claim claim : claims) {
            if("补".equals(claim.getIs_check())){
                check="补";
            }
            if("是".equals(claim.getIs_check())){
                check="是";
                break;
            }
            if("否".equals(claim.getIs_check())){
                check="否";
                break;
            }
        }
        return ClaimDto.convertClaim(foundMapper.selectById(cId).getName(),
                new Claim(check,userMapper.selectById(receiverId).getUsername()));
    }
}
