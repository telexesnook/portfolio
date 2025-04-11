    document.addEventListener("DOMContentLoaded", function () {
        loadTodoList(); // ページ読み込み時に今日の日付のチェックリストを表示
    });

    const taskList = document.getElementById("task-list");

    /* 今日の日付のチェックリストを取得 */
    function loadTodoList() {
        fetch(`/habit-tracking/list/today`)
            .then(response => response.json())
            .then(data => {
                console.log("サーバーからのレスポンスデータ:", data);
                renderChecklist(data.hasHabits, data.todayHabits);
            })
            .catch(error => console.error("データの取得に失敗しました:", error));
    }

    /* 指定された日付のチェックリストを取得 */
    function loadChecklistByDate(dateString) {
        fetch(`/habit-tracking/list/by-date?date=${dateString}`)
            .then(response => response.json())
            .then(data => {
                renderChecklist(true, data, dateString); // renderChecklist は日付ごとのリストを描画する関数
            })
            .catch(error => console.error("チェックリストの取得に失敗しました:", error));
    }

    /* チェックリストの描画関数（共通） */
    function renderChecklist(hasHabits, habitList, dateString) {
        taskList.innerHTML = "";

        // 習慣が存在しない場合
        if (!hasHabits) {
            taskList.innerHTML = `
        <div class="no-habit-box">
            <p class="no-habit-text"> まだ習慣が登録されていません。 </p>
            <button id="go-to-habit-page" class="register-btn"> 習慣を登録する </button>
        </div>
    `;
            document.getElementById("go-to-habit-page").addEventListener("click", () => {
                window.location.href = "/habit";
            });
            return;
        }

        // 選択された日付に習慣が存在しない場合
        if (!Array.isArray(habitList) || habitList.length === 0) {
            taskList.innerHTML = `
        <div class="no-habit-box">
            <p class="no-habit-text"> この日の習慣はありません。 </p>
        </div>
    `;
            return;
        }

        // 日付の有効性を検証する
        const isToday = (dateString) => {
            const today = new Date().toISOString().split("T")[0];
            return dateString === today;
        };

        const isPast = (dateString) => {
            const today = new Date().toISOString().split("T")[0];
            return dateString < today;
        };

        const isFuture = (dateString) => {
            const today = new Date().toISOString().split("T")[0];
            return dateString > today;
        };


        habitList.forEach(habit => {
            const listItem = document.createElement("li");
            listItem.classList.add("task-item");

            const label = document.createElement("label");
            label.classList.add("custom-checkbox");

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.checked = habit.completed === 1;

            const checkmark = document.createElement("span");
            checkmark.classList.add("checkmark");

            label.appendChild(checkbox);
            label.appendChild(checkmark);
            label.appendChild(document.createTextNode(habit.habit_name));

            // チェックボックスの無効化条件を処理する
            if (isPast(dateString) || isFuture(dateString)) {
                checkbox.disabled = true;
                checkbox.classList.add("disabled-checkbox");
            } else {
                checkbox.disabled = false;
                checkbox.addEventListener("change", function () {
                    toggleHabit(habit.habit_id, checkbox.checked);
                    listItem.classList.toggle("completed", checkbox.checked);
                });
            }

            if (checkbox.checked) {
                listItem.classList.add("completed");
            }

            listItem.appendChild(label);
            taskList.appendChild(listItem);
        });

        const spacing = document.createElement("div");
        spacing.style.height = "30px";
        taskList.appendChild(spacing);

        const habitPageLinkBox = document.createElement("div");
        habitPageLinkBox.classList.add("habit-page-link-box");

        // 習慣ページに遷移する
        const habitButton = document.createElement("button");
        habitButton.classList.add("habit-link-btn");
        habitButton.textContent = "習慣ページへ";
        habitButton.onclick = function () {
            window.location.href = "/habit";
        };

        habitPageLinkBox.appendChild(habitButton);
        taskList.appendChild(habitPageLinkBox);
    }


    /* チェックボックスをクリックしたときに完了状態を更新する */
    function toggleHabit(habitId, completed) {
        fetch(`/habit-tracking/toggle/${habitId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ completed: completed ? 1 : 0 })
        })
            .then(response => response.text())
            .then(() => {
                console.log("完了状態の変更に成功しました");
                loadTodoList(); // 今日のチェックリストを再読み込みする
            })
            .catch(error => console.error("完了状態の変更に失敗しました:", error));
    }
