package com.koyoi.main.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class AnnouncementVO {

    private int announcement_id;
    private String admin_id;
    private String user_id;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private String isNew; // 最新のお知らせ

    public String getFormattedCreatedAt() {
        if (created_at != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return created_at.format(formatter);
        }
        return null;
    }

}
