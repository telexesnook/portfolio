package com.koyoi.main.service;

import com.koyoi.main.mapper.AdminMypageMapper;
import com.koyoi.main.vo.AdminMypageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class AdminMypageService {

    private final DataSource dataSource;

    @Autowired
    public AdminMypageService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private AdminMypageMapper adminMypageMapper;

    /* ユーザーリストを取得する */
    public List<AdminMypageVO> getAllUsers() {
        return adminMypageMapper.getAllUsers();
    }

    /* カウンセラーリストを取得する */
    public List<AdminMypageVO> getAllCounselors() {
        return adminMypageMapper.getAllCounselors();
    }


    /* 会員情報を取得する */
    public AdminMypageVO getUserById(String userId) {
        return adminMypageMapper.getUserById(userId);
    }
    
    /* 会員情報を削除する */
    public int deleteUserById(String userId) {
        return adminMypageMapper.deleteUserById(userId);
    }

    /* 会員情報を更新する */
    public int updateUser(AdminMypageVO adminMypageVO) {
        return adminMypageMapper.updateUser(adminMypageVO);
    }


    /* ページネーション処理 */
    public int getUserTotalCount() {
        return adminMypageMapper.selectUserTotalCount();
    }

    public List<AdminMypageVO> getPagedUserList(int offset, int size) {
        return adminMypageMapper.selectUserPage(offset, size);
    }

    public int getCounselorTotalCount() {
        return adminMypageMapper.selectCounselorTotalCount();
    }

    public List<AdminMypageVO> getPagedCounselorList(int offset, int size) {
        return adminMypageMapper.selectCounselorPage(offset, size);
    }


}
