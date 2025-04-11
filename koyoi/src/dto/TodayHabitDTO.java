package com.koyoi.main.dto;

import com.koyoi.main.vo.HabitTrackingVO;
import lombok.Data;

import java.util.List;

@Data
public class TodayHabitDTO {

    private boolean hasHabits;
    private List<HabitTrackingVO> todayHabits;

    public TodayHabitDTO(boolean hasHabits, List<HabitTrackingVO> todayHabits) {
        this.hasHabits = hasHabits;
        this.todayHabits = todayHabits;
    }
}
