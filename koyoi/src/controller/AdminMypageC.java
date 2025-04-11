package com.koyoi.main.controller;

import com.koyoi.main.dto.AdminPageDTO;
import com.koyoi.main.mapper.AdminMypageMapper;
import com.koyoi.main.service.AdminMypageService;
import com.koyoi.main.service.AnnouncementService;
import com.koyoi.main.vo.AdminMypageVO;
import com.koyoi.main.vo.AnnouncementVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminMypageC {

    @Autowired
    private AdminMypageService adminMypageService;

    @Autowired
    private AnnouncementService announcementService;


    @GetMapping("/admin")
    public String admin(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "5") int size,
                        Model model, HttpSession session) {

        // ログインセッション
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            AdminMypageVO user = adminMypageService.getUserById(userId);
            model.addAttribute("user", user);
        }

        int offset = (page - 1) * size;

        // 1. ユーザーリスト（1ページ目）
        List<AdminMypageVO> users = adminMypageService.getPagedUserList(offset, size);
        int userTotal = adminMypageService.getUserTotalCount();

        // 2. カウンセラーリスト（1ページ目）
        List<AdminMypageVO> counselors = adminMypageService.getPagedCounselorList(offset, size);
        int counselorTotal = adminMypageService.getCounselorTotalCount();

        // 3. お知らせリスト（1ページ目）
        List<AnnouncementVO> announcements = announcementService.getPagedAnnouncementList(offset, size);
        int announcementTotal = announcementService.getTotalCount();

        model.addAttribute("users", users);
        model.addAttribute("counselors", counselors);
        model.addAttribute("announcements", announcements);

        model.addAttribute("userTotal", userTotal);
        model.addAttribute("counselorTotal", counselorTotal);
        model.addAttribute("announcementTotal", announcementTotal);

        model.addAttribute("userPage", page);
        model.addAttribute("counselorPage", page);
        model.addAttribute("announcementPage", page);

        return "adminmypage/adminmypage";
    }


    /* 会員リストAPI */
    // ページ単位で取得し、DTOに合計件数とリストを格納して返す
    @GetMapping("/admin/userList")
    @ResponseBody
    public AdminPageDTO<AdminMypageVO> getUserList(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "5") int size) {

        int offset = (page - 1) * size;
        List<AdminMypageVO> users = adminMypageService.getPagedUserList(offset, size);
        int total = adminMypageService.getUserTotalCount();

        return new AdminPageDTO<>(users, total);
    }

    @GetMapping("/admin/counselorList")
    @ResponseBody
    public AdminPageDTO<AdminMypageVO> getcounselorList(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "5") int size) {

        int offset = (page - 1) * size;
        List<AdminMypageVO> users = adminMypageService.getPagedCounselorList(offset, size);
        int total = adminMypageService.getCounselorTotalCount();

        return new AdminPageDTO<>(users, total);
    }

    /* 会員情報の詳細取得 */
    @GetMapping("/admin/userDetail")
    @ResponseBody
    public AdminMypageVO userDetail(String userId) {
        return adminMypageService.getUserById(userId);
    }

    /* 会員情報の削除 */
    @DeleteMapping("/admin/deleteUser")
    @ResponseBody
    public int deleteUser(String userId) {
        return adminMypageService.deleteUserById(userId);
    }

    /* 会員情報の更新 */
    @PostMapping("/admin/updateUser")
    @ResponseBody
    public int updateUser(@RequestBody AdminMypageVO adminMypageVO) {
        return adminMypageService.updateUser(adminMypageVO);
    }


    /* お知らせリストAPI */
    // ページ単位で取得し、DTOに合計件数とリストを格納して返す
    @GetMapping("/admin/announcementList")
    @ResponseBody
    public AdminPageDTO<AnnouncementVO> getAnnouncementList(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "5") int size) {
        int offset = (page - 1) * size;
        List<AnnouncementVO> list = announcementService.getPagedAnnouncementList(offset, size);
        int total = announcementService.getTotalCount();
        return new AdminPageDTO<>(list, total);
    }

    /* お知らせの詳細取得 */
    @GetMapping("/admin/announcementDetail/{id}")
    @ResponseBody
    public AnnouncementVO announcementDetail(@PathVariable("id") int id) {
        return announcementService.getAnnouncementById(id);
    }

    /* お知らせの更新 */
    @PostMapping("/admin/updateAnnouncement")
    @ResponseBody
    public int updateAnnouncemnet(@RequestBody AnnouncementVO announcementVO) {
        return announcementService.updateAnnouncement(announcementVO);
    }

    
    /* お知らせの削除 */
    @DeleteMapping("/admin/deleteAnnouncement")
    @ResponseBody
    public int deleteAnnouncement(@RequestParam("announcement_id") int announcementId) {
        return announcementService.deleteAnnouncement(announcementId);
    }

    /* お知らせの作成 */
    @PostMapping("/admin/createAnnouncement")
    @ResponseBody
    public int createAnnouncement(HttpSession session, @RequestBody AnnouncementVO announcementVO, Model model) {

        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            AdminMypageVO user = adminMypageService.getUserById(userId);
            model.addAttribute("user", user);

            announcementVO.setAdmin_id(userId);
        }

        return announcementService.createAnnouncement(announcementVO);
    }
}
