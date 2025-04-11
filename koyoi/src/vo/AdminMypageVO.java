package com.koyoi.main.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class AdminMypageVO {
    private String user_id;
    private int user_type;
    private String user_name;
    private String user_nickname;
    private String user_email;
    private String user_password;
    private String user_img;
    private LocalDateTime created_at;

    // 変換済みの日付を保存するフィールドを追加
    public String getFormattedCreatedAt() {
        if (created_at != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return created_at.format(formatter);
        }
        return null;
    }



}
