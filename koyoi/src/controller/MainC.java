package com.koyoi.main.controller;

import com.koyoi.main.dto.CompletedDTO;
import com.koyoi.main.dto.TodayHabitDTO;
import com.koyoi.main.service.*;
import com.koyoi.main.vo.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class MainC {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private EmotionService emotionService;

    @Autowired
    private HabitTrackingService habitTrackingService;

    @Autowired
    private AdminMypageService adminMypageService;

    /* メインページ */
    @GetMapping("/main")
    public String main(HttpSession session, Model model) {

        // ログインセッション
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            AdminMypageVO user = adminMypageService.getUserById(userId);
            model.addAttribute("user", user);
        }

        // お知らせモーダルに表示する
        model.addAttribute("announcements", announcementService.getFiveAnnouncements());

        // スライダーに名言データを表示する
        model.addAttribute("quotes", quoteService.getRandomQuotes());

        return "main/main";
    }

    /* 絵文字データの処理 */
    @GetMapping("/calendar/emotions")
    @ResponseBody
    public List<EmotionVO> getAllEmotions(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new IllegalStateException("ログイン情報が存在しません");
        }
        return emotionService.getUserAllEmotions(userId);
    }

    /* ムードスコアの処理 */
    @GetMapping("/mood/scores")
    @ResponseBody
    public List<EmotionVO> getWeeklyMoodScores(HttpSession session, @RequestParam("start") String startDate, @RequestParam("end") String endDate) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new IllegalStateException("ログイン情報が存在しません");
        }

        return emotionService.getWeeklyMoodScores(userId, startDate, endDate);
    }

    /* チェックリスト */
    /* 全習慣リスト（未使用、デフォルト用） */
    @GetMapping("/habit-tracking/list")
    @ResponseBody
    public List<HabitTrackingVO> getHabitTrackingList(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new IllegalStateException("ログイン情報が存在しません");
        }
        List<HabitTrackingVO> habits = habitTrackingService.getHabitTrackingByUser(userId);
        return habits;
    }

    /* 今日の習慣リスト → メインページ読み込み時 */
    @GetMapping("/habit-tracking/list/today")
    @ResponseBody
    public TodayHabitDTO getTodayHabitTrackingList(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new IllegalStateException("ログイン情報が存在しません");
        }

        LocalDate today = LocalDate.now();
        List<HabitTrackingVO> habits = habitTrackingService.getHabitsWithTrackingByDate(userId, today);

        boolean hasHabits = !habits.isEmpty();
        return new TodayHabitDTO(hasHabits, habits);

    }

    /* 指定された日付の習慣リスト → カレンダー日付クリック時 */
    @GetMapping("/habit-tracking/list/by-date")
    @ResponseBody
    public List<HabitTrackingVO> getHabitTrackingListByDate(HttpSession session, @RequestParam("date") String formattedDate) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new IllegalStateException("ログイン情報が存在しません");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(formattedDate, formatter);
        return habitTrackingService.getHabitTrackingByUserAndDate(userId, date);
    }

    /* チェック状態の変更処理 */
    @PostMapping("/habit-tracking/toggle/{habitId}")
    @ResponseBody
    public String toggleHabit(@PathVariable int habitId, @RequestBody CompletedDTO dto, HttpSession session) {

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new IllegalStateException("ログイン情報が存在しません");
        }

        int completed = dto.getCompleted();
        LocalDate today = LocalDate.now();

        habitTrackingService.toggleHabit(userId, habitId, today, completed);
        return "OK";
    }

}

