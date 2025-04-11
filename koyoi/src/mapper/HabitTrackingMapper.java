package com.koyoi.main.mapper;

import com.koyoi.main.vo.HabitTrackingVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HabitTrackingMapper {

    @Select("SELECT ht.tracking_id, ht.habit_id, ht.user_id, ht.completed, ht.weekly_feedback, " +
            "ht.tracking_date, ht.created_at, h.habit_name " +
            "FROM TEST_HABIT_TRACKING ht JOIN TEST_HABIT h ON ht.habit_id = h.habit_id " +
            "WHERE ht.user_id = #{userId} ORDER BY ht.tracking_date DESC")
    List<HabitTrackingVO> getHabitTrackingByUser(String userId);

    @Select("SELECT ht.tracking_id, ht.habit_id, ht.user_id, ht.completed, ht.weekly_feedback, " +
            "ht.tracking_date, ht.created_at, h.habit_name " +
            "FROM TEST_HABIT_TRACKING ht JOIN TEST_HABIT h ON ht.habit_id = h.habit_id " +
            "WHERE ht.user_id = #{userId} AND TRUNC(ht.tracking_date) = TRUNC(sysdate) " +
            "ORDER BY ht.tracking_date DESC")
    List<HabitTrackingVO> getTodayHabitTrackingByUser(String userId);

    @Select("SELECT COUNT(*) FROM TEST_HABIT_TRACKING WHERE user_id = #{userId}")
    int countHabitsByUser(String userId);

    @Update("UPDATE TEST_HABIT_TRACKING SET completed = #{completed} WHERE tracking_id = #{trackingId}")
    void updateHabitCompletion(int trackingId, Integer completed);

    @Select("SELECT ht.tracking_id, ht.habit_id, ht.user_id, ht.completed, ht.weekly_feedback, " +
            "ht.tracking_date, ht.created_at, h.habit_name " +
            "FROM TEST_HABIT_TRACKING ht JOIN TEST_HABIT h ON ht.habit_id = h.habit_id " +
            "WHERE ht.user_id = #{userId} AND TRUNC(ht.tracking_date) = #{date} " +
            "ORDER BY ht.tracking_id")
    List<HabitTrackingVO> getHabitTrackingByUserAndDate(String userId, LocalDate date);

    @Select("SELECT h.habit_id, h.user_id, h.habit_name, " +
            "ht.tracking_id, ht.completed, ht.weekly_feedback, ht.tracking_date, ht.created_at " +
            "FROM TEST_HABIT h " +
            "LEFT JOIN TEST_HABIT_TRACKING ht " +
            "ON h.habit_id = ht.habit_id " +
            "AND TRUNC(ht.tracking_date) = #{date} " +
            "WHERE h.user_id = #{userId} " +
            "ORDER BY h.habit_id")
    List<HabitTrackingVO> getHabitsWithTrackingByDate(String userId, LocalDate date);

    // 今日の該当 Habit に対する記録が存在するかを確認する
    @Select("SELECT COUNT(*) FROM TEST_HABIT_TRACKING WHERE user_id = #{userId} AND habit_id = #{habitId} AND TRUNC(tracking_date) = #{date}")
    Integer checkTrackingExists(String userId, int habitId, LocalDate date);

    // 記録がある場合は completed を更新する
    @Update("UPDATE TEST_HABIT_TRACKING SET completed = #{completed} " +
            "WHERE user_id = #{userId} AND habit_id = #{habitId} AND TRUNC(tracking_date) = #{date}")
    void updateTracking(String userId, int habitId, LocalDate date, int completed);

    // 記録がない場合は新規で insert する
    @Insert("INSERT INTO TEST_HABIT_TRACKING (user_id, habit_id, tracking_date, completed, created_at) " +
            "VALUES (#{userId}, #{habitId}, #{date}, #{completed}, SYSDATE)")
    void insertTracking(String userId, int habitId, LocalDateTime date, int completed);
}
