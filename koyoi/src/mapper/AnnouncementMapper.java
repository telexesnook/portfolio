package com.koyoi.main.mapper;

import com.koyoi.main.vo.AnnouncementVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnnouncementMapper {

    /* すべてのお知らせを取得する */
    @Select("SELECT A.announcement_id, A.admin_id, U.user_id, A.title, A.content, A.created_at " +
            "FROM TEST_ANNOUNCEMENT A " +
            "LEFT JOIN TEST_USER U ON A.admin_id = U.user_id " +
            "ORDER BY A.created_at DESC")
    List<AnnouncementVO> getAllAnnouncements();

    /* 最新のお知らせを5件取得する */
    @Select("SELECT * FROM ( " +
            "SELECT A.announcement_id, A.admin_id, U.user_id, A.title, A.content, A.created_at " +
            "FROM TEST_ANNOUNCEMENT A " +
            "LEFT JOIN TEST_USER U ON A.admin_id = U.user_id " +
            "ORDER BY A.created_at DESC " +
            ") WHERE ROWNUM <= 5")
    List<AnnouncementVO> getFiveAnnouncements();

    /* お知らせの詳細を取得する */
    @Select("SELECT * FROM TEST_ANNOUNCEMENT WHERE announcement_id = #{id}")
    AnnouncementVO getAnnouncementById(int id);

    /* お知らせを更新する */
    @Update("UPDATE TEST_ANNOUNCEMENT SET title = #{title}, content = #{content} WHERE announcement_id = #{announcement_id}")
    int updateAnnouncement(AnnouncementVO announcementVO);

    /* お知らせを削除する */
    @Delete("DELETE FROM TEST_ANNOUNCEMENT WHERE announcement_id = #{announcementId}")
    int deleteAnnouncement(int announcementId);

    /* お知らせを作成する */
    @Insert("INSERT INTO TEST_ANNOUNCEMENT (admin_id, title, content, created_at)" +
            "VALUES (#{admin_id}, #{title}, #{content}, SYSDATE)")
    int createAnnouncement(AnnouncementVO announcementVO);

    
    /* お知らせのページネーション処理 */
    @Select("SELECT * FROM TEST_ANNOUNCEMENT ORDER BY created_at DESC OFFSET #{offset} ROWS FETCH NEXT #{size} ROWS ONLY")
    List<AnnouncementVO> selectAnnouncementPage(int offset, int size);

    @Select("SELECT COUNT(*) FROM TEST_ANNOUNCEMENT")
    int selectAnnouncementTotalCount();
}
