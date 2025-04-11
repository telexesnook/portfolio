<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="/static/js/announcement/announcement.js" defer></script>
<link rel="stylesheet" href="/static/css/announcement/announcement.css">
<title>KOYOI</title>
<link href="https://fonts.googleapis.com/css2?family=Inknut+Antiqua&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<body data-total-count="${totalCount}">

    <h2 id="table-title" class="table-title"> お知らせ </h2>

    <div id="announcementTable" class="announcement-board">
        <div class="announcement-board-header">
            <div class="col col-announcement-num"> 番号 </div>
            <div class="col col-announcement-id"> 管理者ID </div>
            <div class="col col-announcement-title"> タイトル </div>
            <div class="col col-announcement-created"> 作成日 </div>
        </div>
        <c:forEach var="announcement" items="${announcements}" varStatus="status">
            <div class="announcement-row announcement-detail-btn" data-user-id="${announcement.announcement_id}">
                <div class="cell col-announcement-num">${totalCount - ((currentPage - 1) * 5 + status.index)}</div>
                <div class="cell col-announcement-id">${announcement.admin_id}</div>
                <div class="cell col-announcement-title">${announcement.title}</div>
                <div class="cell col-announcement-created">${announcement.formattedCreatedAt}</div>
            </div>
        </c:forEach>
    </div>
    <div id="commonPagination" class="pagination"></div>


