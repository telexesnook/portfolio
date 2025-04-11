package com.koyoi.main.mapper;

import com.koyoi.main.vo.EmotionVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EmotionMapper {

    // 感情データを登録する（emotion_emoji を含む）
    @Insert("INSERT INTO TEST_EMOTION (user_id, diary_id, emotion_emoji, emotion_score, recorded_at) " +
            "VALUES (#{user_id}, #{diary_id}, #{emotion_emoji}, #{emotion_score}, #{recorded_at})")
    int addEmotion(EmotionVO emotionVO);

    // 感情の絵文字と recorded_at を同時に更新する
    @Update("UPDATE TEST_EMOTION " +
            "SET emotion_emoji = #{emotion_emoji}, " +
            "    recorded_at = #{recorded_at} " +
            "WHERE diary_id = #{diary_id} " +
            "AND user_id = #{user_id}")
    int updateEmotionWithDate(@Param("diary_id") int diaryId,
                              @Param("emotion_emoji") String emotionEmoji,
                              @Param("recorded_at") LocalDateTime recordedAt,
                              @Param("user_id") String userId);

    // 今日のスコアのみ更新する（モーダルからスコアを入力して呼び出す）
    @Update("UPDATE TEST_EMOTION " +
            "SET emotion_score = #{emotion_score} " +
            "WHERE diary_id = #{diary_id} " +
            "AND user_id = #{user_id}")
    int updateEmotionScore(@Param("diary_id") int diaryId,
                           @Param("emotion_score") int emotionScore,
                           @Param("user_id") String userId);

    // 今日のスコアを削除する
    @Delete("DELETE FROM TEST_EMOTION " +
            "WHERE diary_id = #{diaryId} " +
            "AND user_id = #{userId}")
    int deleteEmotion(@Param("diaryId") int diaryId, @Param("userId") String userId);

    /* 絵文字 */
    @Select("SELECT E.emotion_id, E.user_id, E.diary_id, E.emotion_score, E.emotion_emoji, E.recorded_at " +
            "FROM TEST_EMOTION E LEFT JOIN TEST_USER U ON E.user_id = U.user_id " +
            "LEFT JOIN TEST_DIARY D ON E.diary_id = D.diary_id " +
            "WHERE E.user_id = #{userId} " +
            "ORDER BY E.recorded_at ASC")
    List<EmotionVO> getAllUserEmotions(String userId);

    /* ムードスコア */
    @Select("SELECT E.emotion_id, E.user_id, E.diary_id, E.emotion_score, E.emotion_emoji, E.recorded_at " +
            "FROM TEST_EMOTION E LEFT JOIN TEST_USER U ON E.user_id = U.user_id " +
            "LEFT JOIN TEST_DIARY D ON E.diary_id = D.diary_id " +
            "WHERE E.user_id = #{userId} " +
            "AND TRUNC(E.recorded_at) BETWEEN " +
            "TO_DATE(#{startDate}, 'YYYY-MM-DD') AND TO_DATE(#{endDate}, 'YYYY-MM-DD') " +
            "ORDER BY E.recorded_at ASC")
    List<EmotionVO> getWeeklyMoodScores(String userId, String startDate, String endDate);
}
