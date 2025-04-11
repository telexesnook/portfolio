package com.koyoi.main.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuoteVO {

    private int quote_id;
    private String admin_id;
    private String user_id;
    private String content;
    private LocalDateTime created_at;

}
