package org.example.lostandfound.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

    private int id;
    private int senderId;
    private int receiverId;
    private String sender;
    private String receiver;
    private String reason;
    private String is_check;
    private int c_id;

    public Claim(String is_check, String receiver) {
        this.is_check = is_check;
        this.receiver = receiver;
    }
}
