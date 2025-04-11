document.addEventListener("DOMContentLoaded", function () {

    // モーダル
    const noticeBtn = document.getElementById("notice");
    const modal = document.getElementById("notice-modal");
    const closeBtn = document.querySelector(".close-btn");


    noticeBtn.addEventListener("click", function (event) {
        event.stopPropagation(); // 他のクリックイベントを無効化（イベントバブリング防止）
        modal.style.display = modal.style.display === "block" ? "none" : "block";
        // modal.style.display → CSSファイルのスタイルは取得できないため、空文字列が返る
        // 三項演算子で false と判定され、block が設定される → モーダルを表示
        // 以降は JavaScript 側で設定した表示状態を使用
    });


    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });


    document.addEventListener("click", function (event) {
        if (!modal.contains(event.target) && event.target !== noticeBtn) {
            modal.style.display = "none";
        }
        // event.target → ユーザーがクリックした要素（HTML タグ）
        // つまり、クリックした要素がモーダル内でない、またはお知らせボタンでない場合
        // モーダルを閉じる
    });

});
