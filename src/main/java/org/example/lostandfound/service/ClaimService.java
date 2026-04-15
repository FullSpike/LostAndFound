package org.example.lostandfound.service;

import org.example.lostandfound.dto.ClaimDto;

import java.util.List;
import java.util.Map;

public interface ClaimService {
    List<Map<String, Object>> getClaim(int senderId, int receiverId, int c_id);

    List<Map<String, Object>> getMyClaim(int receiverId);

    void addClaim(int senderId, int receiverId, int cId, String reason);

    void agreeClaim(int id);

    void rejectClaim(int id);

    void requireReasonClaim(int id);

    List<ClaimDto> getClaimMessage(int senderId);

    ClaimDto getClaimDto(int senderId, int receiverId, int cId);
}
