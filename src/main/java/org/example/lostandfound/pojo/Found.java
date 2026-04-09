package org.example.lostandfound.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Found {

    private int id;
    private String name;
    private String location;

    private String time;

    private String description;
    private String pathName;
    private String status;
    private String node;
    private int f_id;
    private  String phone;
    private  String is_report;
    private  String report_reason;
}
