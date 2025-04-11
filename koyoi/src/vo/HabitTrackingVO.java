package com.koyoi.main.vo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class HabitTrackingVO {

    private int tracking_id;
    private int habit_id;
    private String user_id;
    private Integer completed;
    private String weekly_feedback;
    private Date tracking_date;
    private Timestamp created_At;
    private String habit_name; // JOIN を使って取得する習慣名

}
