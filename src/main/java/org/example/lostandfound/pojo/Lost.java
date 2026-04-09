package org.example.lostandfound.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Lost {

    private int id;
    private String name;
    private String location;


    private String time;

    private String description;
    private String pathName;
    private String status;
    private String note;

    private int l_id;
    private  String phone;
    private  String is_report;
    private  String report_reason;
    private  String need_top;


}
