package com.koyoi.main.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminPageDTO<T> {

    private List<T> list;
    private int total;

    public AdminPageDTO(List<T> list, int total) {
        this.list = list;
        this.total = total;
    }


}
