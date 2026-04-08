package org.example.lostandfound.pojo;


import lombok.Data;

@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String status;
    private String avatar;
    private String token;

   }
