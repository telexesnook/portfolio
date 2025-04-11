<%@ page import="com.koyoi.main.vo.AdminMypageVO" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- ログイン機能 --%>  
<%
    HttpSession session1 = request.getSession(false); // 既存のセッションを取得する
    String userId = null;
    String userType = null;
    String userNickName = "친구";
    String userImg = null;

    if (session1 != null) {
        userId = (String) session1.getAttribute("userId"); // セッションに保存された userId の値
        userImg = (String) session1.getAttribute("userImg"); // セッションに保存された userImg の値
                                                        
        String nicknameFromSession = (String) session1.getAttribute("userNickName"); 
        if (nicknameFromSession != null) {
            userNickName = nicknameFromSession;
        }

        Object userTypeObj = session1.getAttribute("userType"); // int 型で保存されている場合
        if (userTypeObj != null) {
            userType = userTypeObj.toString(); // int を String に変換する
        }
    }

    if (userId == null) {
        response.sendRedirect("/login"); // セッションが存在しない、または有効期限切れの場合はログインページに遷移する
        return;
    }
%>

<!DOCTYPE html>
<html lang="ja">
<head>
    <title>KOYOI</title>
    <link href="https://fonts.googleapis.com/css2?family=Inknut+Antiqua&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/main/main.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    <script>
        /* userType によってそれぞれのマイページへ遷移する */
        /* user_type => 1 : 一般会員 / 2 : カウンセラー / 3 : 管理者 */
        var userType = "<%= userType %>";

        function goToMyPage() {
            if (userType === "1") {
                location.href = "/usermypage";
            } else if (userType === "2") {
                location.href = "/counselormypage";
            }
        }
    </script>
    <style>
        @font-face {
            font-family: 'MyFont';
            src: url('/static/fonts/Boku2-Regular.otf') format('opentype');
        }

        body {
            font-family: 'MyFont', sans-serif;
            font-size: 32px;
            color: black;
        }
    </style>

</head>
<body>

<div class="container">

    <%-- ヘッダー --%>
    <header class="header-bar">
        <div class="logo-container">
            <a href="/main"> <img class="logo-icon" src="/static/imgsource/layout/koyoi_name.png" alt="KOYOI"> </a>
        </div>
        <div class="header-icons">
            <button class="header-btn" id="notice">
                <img src="/static/imgsource/main/notice.png" alt="notice">
            </button>
            <button class="header-btn">
                <a href="/logout"> <img src="/static/imgsource/layout/logout.png" alt="logout"> </a>
            </button>
            <button class="profile-btn" onclick="goToMyPage()">
                <img class="profile-img" src="${user.user_img}" onerror="this.onerror=null; this.src='/imgsource/userProfile/default.png'" alt="profile">
            </button>
        </div>
    </header>

    <%-- お知らせモーダルウィンドウ --%>
    <div id="notice-modal" class="modal">
        <div class="modal-content">
            <span class="close-btn"> &times; </span>
            <h3 class="modal-title"> <a href="/announcement">  お知らせ  </a> </h3>
            <ul id="notice-lists">
                <c:forEach var="announcement" items="${announcements}">
                    <li>
                        <i class="fa-solid fa-moon notice-icon"></i>
                        <a href="/announcement/view/${announcement.announcement_id}">${announcement.title}</a>
                        <c:if test="${announcement.isNew == 'Y'}">
                        <span class="new-tag"> New </span>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>

    <%-- メインコンテンツ --%>
    <main class="main-container">

        <%-- 名言スライド --%>
        <div class="quotes-container">
            <div class="swiper">
                <div class="swiper-wrapper" id="quoteWrapper">
                    <c:forEach var="quote" items="${quotes}">
                        <div class="swiper-slide">
                            <span class="quote-symbol"> ❝ </span>
                            ${quote.content}
                            <span class="quote-symbol"> ❞ </span>
                        </div>
                    </c:forEach>
                </div>
                <div class="swiper-button-prev"></div>
                <div class="swiper-button-next"></div>
            </div>
        </div>
        <%-- swiper.js ライブラリ --%>
        <script>
            const swiper = new Swiper('.swiper', {
                direction: 'horizontal',
                loop: true,
                autoplay: {
                    delay: 60000,   // 1分
                    disableOnInteraction: false, // スライドをタッチしてもオートプレイが継続される
                },

                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                }
            });
        </script>

        <div class="content-wrapper">
            <%-- 左エリア：カレンダー --%>
            <div class="left-content">
                <div class="calendar-container">
                    <jsp:include page="maincalendar.jsp"/>
                </div>
            </div>

            <%-- 右エリア：チェックリスト＋ムードグラフ＋チャットボット --%>
            <div class="right-content">
                <div class="right-inner">
                    <div class="checklist-container">
                        <h3> 本日のタスク </h3>
                        <ul id="task-list"></ul>
                    </div>

                    <div class="right-side">

                        <div class="mood-chart">
                            <h3> 気分チャート </h3>
                            <canvas id="moodChart"></canvas>
                        </div>

                        <div class="chat-connect">
                            <!-- チャットボットエリア -->
                            <div class="chatbot-bubble-container">
                                <p class="chatbot-guide-text">ちょっとしたお悩みは、</p>
                                <button class="chatbot" onclick="openChatModal()">チャットボット</button>
                                <img class="chatbot_menuimg" src="/static/imgsource/layout/sittingwoman.png" onclick="openChatModal()">
                            </div>

                            <!-- ライブチャットエリア -->
                            <div class="livechat-bubble-container">
                                <p class="livechat-guide-text"> 深いお悩みは、</p>
                                <button class="livechat" onclick="goToLiveChat()">ライブ相談</button>
                                <img class="livechat_menuimg" src="/static/imgsource/login/mainbanner4.jpg" onclick="goToLiveChat()">
                            </div>
                        </div>

                    </div>
            </div>
        </div>
    </main>

</div>

<script src="/static/js/main/todoList.js"></script>
<script src="/static/js/main/main.js"></script>
<script>
    const userName = "<%= userNickName %>";
        function goToLiveChat() {
        if (userType === "2") {
        location.href = "/counselormypage";
    } else {
        location.href = "/livechatreservation";
    }
    }
</script>
<script src="/static/js/chat/chat-modal.js"></script>
</body>
</html>