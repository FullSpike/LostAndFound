package org.example.lostandfound.pojo;


import lombok.Data;

@Data
public class Message {
    private int id;
    private int senderId;
    private int receiverId;
    private String content;
    private String time;
    private String is_read;
}
