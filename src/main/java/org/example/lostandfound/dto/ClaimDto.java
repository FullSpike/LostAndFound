package org.example.lostandfound.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lostandfound.pojo.Claim;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDto {


    private String name;
    private String is_check;
    private String receiver;

    public static ClaimDto convertClaim(String name, Claim claim) {
        ClaimDto claimDto = new ClaimDto();
        claimDto.setName(name);
        claimDto.setIs_check(claim.getIs_check());
        claimDto.setReceiver(claim.getReceiver());
        return claimDto;
    }

}
