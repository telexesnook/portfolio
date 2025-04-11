package com.koyoi.main.service;

import com.koyoi.main.mapper.EmotionMapper;
import com.koyoi.main.vo.EmotionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmotionService {

    private final DataSource dataSource;

    @Autowired
    public EmotionService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private EmotionMapper emotionMapper;

    public List<EmotionVO> getUserAllEmotions(String userId) {
        return emotionMapper.getAllUserEmotions(userId);
    }

    public List<EmotionVO> getWeeklyMoodScores(String userId, String startDate, String endDate) {
        return emotionMapper.getWeeklyMoodScores(userId, startDate, endDate);
    }
}
