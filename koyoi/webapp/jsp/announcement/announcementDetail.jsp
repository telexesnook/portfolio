<%@ page import="com.koyoi.main.vo.AdminMypageVO" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/static/css/announcement/announcementDetail.css">
<div class="announcement-view">
    <div class="title">${announcement.title}</div>
    <div class="meta">
        投稿者: ${announcement.admin_id} | 投稿日: ${announcement.formattedCreatedAt}
    </div>
    <div class="content">${announcement.content}</div>

    <a href="/announcement" class="btn-back">← 一覧に戻る</a>
</div>


