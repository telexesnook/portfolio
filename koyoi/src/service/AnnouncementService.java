package com.koyoi.main.service;

import com.koyoi.main.mapper.AnnouncementMapper;
import com.koyoi.main.vo.AnnouncementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AnnouncementService {

    private final DataSource dataSource;

    @Autowired
    public AnnouncementService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private AnnouncementMapper announcementMapper;

    /* お知らせ全件の取得（未使用） */
    public List<AnnouncementVO> getAllAnnouncements() {
        List<AnnouncementVO> announcements = announcementMapper.getAllAnnouncements();
        return announcements;
    }

    /* 最新のお知らせを5件取得する */
    public List<AnnouncementVO> getFiveAnnouncements() {
        List<AnnouncementVO> announcements = announcementMapper.getFiveAnnouncements();
        LocalDateTime threeDaysAgo = LocalDateTime.now().minus(3, ChronoUnit.DAYS); // 現在時刻から3日間を計算する

        // 作成日が3日以内のお知らせに「New」マークを表示する
        for (AnnouncementVO announcement : announcements) {
            if (announcement.getCreated_at().isAfter(threeDaysAgo)) {
                announcement.setIsNew("Y");  // 3日以内であれば VO に "Y" を保存して「New」を表示する
            } else {
                announcement.setIsNew("N");  // 古いお知らせは「New」マークを表示しない（VO に "N" を保存）
            }
        }
        return announcements;
    }

    /* お知らせの詳細を取得する */
    public AnnouncementVO getAnnouncementById(int id) {
        return announcementMapper.getAnnouncementById(id);
    }

    /* お知らせを更新する */
    public int updateAnnouncement(AnnouncementVO announcementVO) {
        return announcementMapper.updateAnnouncement(announcementVO);
    }

    /* お知らせを削除する */
    public int deleteAnnouncement(int announcementId) {
        return announcementMapper.deleteAnnouncement(announcementId);
    }

    /* お知らせを作成する */
    public int createAnnouncement(AnnouncementVO announcementVO) {
        return announcementMapper.createAnnouncement(announcementVO);
    }


    /* お知らせのページネーション処理 */
    public List<AnnouncementVO> getPagedAnnouncementList(int offset, int size) {
        return announcementMapper.selectAnnouncementPage(offset, size);
    }

    public int getTotalCount() {
        return announcementMapper.selectAnnouncementTotalCount();
    }


}
