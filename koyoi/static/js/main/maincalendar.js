let currentMode = "daily"; // 初期モードは Daily
let currentDate = new Date();
let emotionMap = {}; // 感情データを保存

document.getElementById("prevCalendar").addEventListener("click", switchCalendar);
document.getElementById("nextCalendar").addEventListener("click", switchCalendar);

/* Daily ↔ Weekly 表示の切り替え */
function switchCalendar() {
    if (currentMode === "daily") {
        document.getElementById("calendar-title").innerText = "ウィークリー";
        currentMode = "weekly";
    } else {
        document.getElementById("calendar-title").innerText = "デイリー";
        currentMode = "daily";
    }
    generateCalendar();
}

/* 前月・次月への移動処理 */
document.getElementById("prevMonth").addEventListener("click", function () {
    currentDate.setMonth(currentDate.getMonth() - 1);
    generateCalendar();
});

document.getElementById("nextMonth").addEventListener("click", function () {
    currentDate.setMonth(currentDate.getMonth() + 1);
    generateCalendar();
});

/* 絵文字データを取得 */
function fetchEmotions() {
    fetch(`/calendar/emotions`)
        .then(response => response.json())
        .then(emotionData => {
            emotionMap = {};
            emotionData.forEach(event => {
                let formattedDate = event.recorded_at.substring(0, 10);
                emotionMap[formattedDate] = event.emotion_emoji;
            });
            generateCalendar();
        })
        .catch(error => console.error("感情データの取得に失敗しました。", error));
}

/* カレンダーを生成 */
function generateCalendar() {
    let calendarEl = document.getElementById("calendar-grid");
    let monthYearEl = document.getElementById("calendar-month-year");

    const monthNames = [
        "1月", "2月", "3月", "4月", "5月", "6月",
        "7月", "8月", "9月", "10月", "11月", "12月"
    ];

    let year = currentDate.getFullYear();
    let month = currentDate.getMonth() + 1;
    let formattedMonth = `${year}-${String(month).padStart(2, '0')}`; // "2025-03"

    monthYearEl.innerText = `${year}年 ${monthNames[month - 1]}`;
    calendarEl.innerHTML = "";

    let firstDay = new Date(year, month - 1, 1).getDay(); // 月初日の曜日
    let lastDate = new Date(year, month, 0).getDate(); // 今月の最終日
    let prevLastDate = new Date(year, month - 1, 0).getDate(); // 前月の最終日
    let totalCells = 0;

    let today = new Date();
    let todayFormatted = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;

    /* カレンダーセルの構成 */

    // 1. 前月の空白セル
    for (let i = firstDay - 1; i >= 0; i--) {
        let emptyDiv = document.createElement("div");
        emptyDiv.classList.add("calendar-day", "inactive");
        emptyDiv.innerText = prevLastDate - i;
        calendarEl.appendChild(emptyDiv);
        totalCells++;
    }

    // 2. 今月の日付セル
    for (let date = 1; date <= lastDate; date++) {
        let dayDiv = document.createElement("div");
        dayDiv.classList.add("calendar-day");

        let formattedDate = `${formattedMonth}-${String(date).padStart(2, '0')}`;
        dayDiv.dataset.data = formattedDate;

        // データがある場合は絵文字を表示、ない場合は日付を表示
        if (currentMode === "daily" && emotionMap[formattedDate]) {
            dayDiv.innerHTML = `<span>${emotionMap[formattedDate]}</span>`;
        } else {
            dayDiv.innerText = date;
        }

        // 本日の日付を強調表示
        if (formattedDate === todayFormatted) {
            dayDiv.classList.add("today");
        }

        // Daily カレンダーで日付をクリックすると日記ページへ遷移
        if (currentMode === "daily") {
            dayDiv.addEventListener("click", function () {
                let selectedDate = new Date(`${formattedDate}T00:00:00`); // クリックした日付を Date オブジェクトに変換

                if (selectedDate > today) {
                    alert("未来の日付には日記を記入できません。");
                    return;
                }


                // 日付をサーバーに送信してセッションに保存
                fetch('/diary/setSelectedDate', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ date: formattedDate })
                })
                .then(() => {
                    window.top.location.href = '/diary'; // 保存成功時に日記ページに遷移
                })
                .catch(err => {
                    console.error("	サーバーセッションの保存に失敗しました:", err);
                });

            });

          // Weekly カレンダーで日付をクリックするとムードチャートとチェックリストを読み込む
        } else if (currentMode === "weekly") {
            dayDiv.addEventListener("click", function () {
                let selectedDate = new Date(`${formattedDate}T00:00:00`);

                document.querySelectorAll(".calendar-day").forEach(el => {
                    el.classList.remove("selected-date");
                });

                // 現在クリックされた日付にのみスタイルを追加
                this.classList.add("selected-date");
                selectedCalendarDate = this;

                if (selectedDate) {
                    let weekRange = getWeekRange(selectedDate);
                    fetchWeeklyMoodScores(selectedDate);
                    console.log("クリックされた日付:", formattedDate);
                    loadChecklistByDate(formattedDate);
                } else {
                    console.log("選択された日付が未定義です（undefined）");
                }
            })
        }

        calendarEl.appendChild(dayDiv);
        totalCells++;
    }

    // 3. 翌月の空白セル
    while (totalCells < 42) {
        let emptyDiv = document.createElement("div");
        emptyDiv.classList.add("calendar-day", "inactive");
        emptyDiv.innerText = totalCells - lastDate - firstDay + 1;
        calendarEl.appendChild(emptyDiv);
        totalCells++;
    }
}

document.addEventListener("DOMContentLoaded", function () {
    fetchEmotions(); // 感情データを取得
    let today = new Date();
    today = new Date(today.getFullYear(), today.getMonth(), today.getDate()); // Date オブジェクトから時間情報を削除
    let { start, end } = getWeekRange(today);
    fetchWeeklyMoodScores(today);
});

/* クリックした日付が含まれる週を計算して、yyyy-MM-dd 形式の文字列で返す */
function getWeekRange(selectedDate) {
    let date = new Date(selectedDate); // 選択した日付を Date オブジェクトに変換
    let dayOfWeek = date.getDay(); // 曜日を取得（0: 日曜日, 1: 月曜日, ..., 6: 土曜日）

    let monday = new Date(date);
    monday.setDate(date.getDate() - (dayOfWeek === 0 ? 5 : dayOfWeek -2));

    let sunday = new Date(monday);
    sunday.setDate(monday.getDate() + 6);

    console.log("選択された日付が含まれる週の月曜日:", monday.toISOString().split("T")[0]);
    console.log("選択された日付が含まれる週の日曜日:", sunday.toISOString().split("T")[0]);

    return {
        start: monday.toISOString().split("T")[0], // yyyy-MM-dd 形式の文字列として返す
    };
}

/* 気分スコアを取得 */
function fetchWeeklyMoodScores(selectedDate) {
    let { start, end } = getWeekRange(selectedDate);

    console.log(`選択された日付: ${selectedDate}, 週間範囲: ${start} ~ ${end}`);

    fetch(`/mood/scores?start=${start}&end=${end}`)
        .then(response => response.json())
        .then(moodData => {
            let moodScores = new Array(7).fill(0); // 月曜日から始まる配列を作成
            moodData.forEach(entry => {
                let date = new Date(entry.recorded_at);
                let dayIndex = date.getDay();

                if (dayIndex === 0) dayIndex = 6; // 日曜日（0）を配列の最後に移動
                else dayIndex -= 1; // 曜日インデックスを調整（月〜日: 0〜6）

                moodScores[dayIndex] = entry.emotion_score; // スコアを保存
            });
            updateMoodChart(moodScores);
        })
        .catch(error => console.error("週間の気分スコアの取得に失敗しました。", error));
}

/* グラフの更新処理 */
function updateMoodChart(moodScores) {
    let chartElement = document.getElementById('moodChart');

    if (!chartElement) {
        console.error("moodChart 要素が見つかりません。");
        return;
    }

    const ctx = chartElement.getContext('2d'); // グラフの canvas 要素の役割

    // 既存のチャートが `<canvas>` 要素かを確認し、初期化する
    if (window.moodChart && !(window.moodChart instanceof Chart)) {
        window.moodChart = null;
    }

    if (window.moodChart instanceof Chart) {
        window.moodChart.destroy();
    }

    // 新しいチャートを生成する
    window.moodChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['月', '火', '水', '木', '金', '土', '日'],
            datasets: [{
                label: '今週の気分',
                data: moodScores,
                tension: 0.2,
                fill: true,
                backgroundColor: 'rgba(233, 215, 233, 0.3)',
                borderColor: '#A1887F',
                pointBackgroundColor: '#C8E3D4',
                pointBorderColor: '#D2C4D6',
                pointRadius: 4,
                pointHoverRadius: 8,
                pointHoverBackgroundColor: '#EAD4C2',
                pointHoverBorderColor: '#5D4037',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#7B6651'
                    },
                    grid: {
                        display: false
                    }
                },
                y: {
                    beginAtZero: true,
                    max: 100,
                    ticks: {
                        color: '#A1887F'
                    },
                    grid: {
                        color: '#f3e7db'
                    }
                }
            }
        }
    });

    console.log("チャートが正常に更新されました。");
}
