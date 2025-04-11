package com.koyoi.main.service;

import com.koyoi.main.mapper.HabitTrackingMapper;
import com.koyoi.main.vo.HabitTrackingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HabitTrackingService {

    @Autowired
    private HabitTrackingMapper habitTrackingMapper;

    public List<HabitTrackingVO> getHabitTrackingByUser(String userId) {
        return habitTrackingMapper.getHabitTrackingByUser(userId);
    }

    public List<HabitTrackingVO> getTodayHabitTrackingByUser(String userId) {
        return habitTrackingMapper.getTodayHabitTrackingByUser(userId);
    }

    public boolean countHabitTrackingByUser(String userId) {
        return habitTrackingMapper.countHabitsByUser(userId) > 0;
    }

    public void toggleHabitCompletion(int trackingId, Integer completed) {
        habitTrackingMapper.updateHabitCompletion(trackingId, completed);
    }

    // 指定された日付を取得する
    public List<HabitTrackingVO> getHabitTrackingByUserAndDate(String userId, LocalDate date) {
        return habitTrackingMapper.getHabitsWithTrackingByDate(userId, date);
    }

    // 習慣データを取得する
    public List<HabitTrackingVO> getHabitsWithTrackingByDate(String userId, LocalDate date) {
        return habitTrackingMapper.getHabitsWithTrackingByDate(userId, date);
    }

    // 習慣チェック処理
    public void toggleHabit(String userId, int habitId, LocalDate date, int completed) {

        Integer exists = habitTrackingMapper.checkTrackingExists(userId, habitId, date);
        if (exists != null && exists > 0) {
            habitTrackingMapper.updateTracking(userId, habitId, date, completed);
        } else {
            LocalDateTime now = LocalDateTime.now();
            habitTrackingMapper.insertTracking(userId, habitId, now, completed);
        }

    }
}
