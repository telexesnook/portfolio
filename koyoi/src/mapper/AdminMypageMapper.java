package com.koyoi.main.mapper;

import com.koyoi.main.vo.AdminMypageVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMypageMapper {

    /* ユーザータイプに応じてデータを取得する */
    @Select("select * from test_user where user_type = 1 order by created_at DESC")
    List<AdminMypageVO> getAllUsers();

    @Select("select * from test_user where user_type = 2 order by created_at DESC")
    List<AdminMypageVO> getAllCounselors();


    /* user_id に基づいてユーザー情報を取得・削除・更新する */
    @Select("select * from test_user where user_id = #{userId}")
    AdminMypageVO getUserById(@Param("userId") String userId);

    @Delete("delete from test_user where user_id = #{userId}")
    int deleteUserById(String userId);

    @Update("update test_user set user_password = #{user_password}, user_nickname = #{user_nickname}, user_email = #{user_email} where user_id = #{user_id}")
    int updateUser(AdminMypageVO adminMypageVO);


    /* ページネーション処理 */
    @Select("SELECT COUNT(*) FROM TEST_USER WHERE user_type = 1")
    int selectUserTotalCount();

    @Select("SELECT * FROM TEST_USER WHERE user_type = 1 ORDER BY created_at DESC OFFSET #{offset} ROWS FETCH NEXT #{size} ROWS ONLY")
    List<AdminMypageVO> selectUserPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM TEST_USER WHERE user_type = 2 ORDER BY created_at DESC OFFSET #{offset} ROWS FETCH NEXT #{size} ROWS ONLY")
    List<AdminMypageVO> selectCounselorPage(int offset, int size);

    @Select("SELECT COUNT(*) FROM TEST_USER WHERE user_type = 2")
    int selectCounselorTotalCount();
}
