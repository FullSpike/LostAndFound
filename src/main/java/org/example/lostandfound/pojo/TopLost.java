package org.example.lostandfound.pojo;


import lombok.Data;

@Data
public class TopLost {
    private int id;
    private String name;
    private String location;

    private String time;

    private String description;
    private String pathName;
    private String status;
    private String node;
    private int l_id;
    private  String phone;
    private  String is_report;
    private  String report_reason;
}
