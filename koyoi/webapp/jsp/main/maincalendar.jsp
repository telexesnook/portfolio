<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>カレンダー</title>
    <link href="https://fonts.googleapis.com/css2?family=Inknut+Antiqua&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/main/main.css">
</head>
<body>

<div class="calendar">
    <div class="calendar-header">
        <button id="prevMonth"> ◀ </button>
        <h3 id="calendar-month-year"></h3>
        <button id="nextMonth"> ▶ </button>

        <div id="calendar-title-container">
            <button id="prevCalendar"> ◀ </button>
            <span id="calendar-title"> デイリー </span>
            <button id="nextCalendar"> ▶ </button>
        </div>
    </div>

    <div class="calendar-weeks">
        <div> 日 </div> <div> 月 </div> <div> 火 </div>
        <div> 水 </div> <div> 木 </div> <div> 金 </div> <div> 土 </div>
    </div>
    <div class="calendar-days" id="calendar-grid"></div>
</div>

<script src="/static/js/main/maincalendar.js"></script>
</body>
</html>
