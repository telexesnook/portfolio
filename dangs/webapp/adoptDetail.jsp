<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상세 페이지</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link rel="stylesheet" href="css/hy/adoptDetail.css">
<script type="text/javascript" src="js/hy/adoptDetail.js" defer></script>
</head>
<body>

	<!-- ナビゲーションバー -->
	<nav class="navigation-bar">
		<ul class="nav-links">
			<li><a href="AdoptionController?action=shelter">보호소 입양 공고</a></li> // 保護施設の譲渡公告
			<li><a href="#" id=favoritesTab>관심있는 공고</a></li> // 気になる公告
		</ul>
	</nav>

	<div class="main-container">
		<!-- ページヘッダー -->
		<header class="page-header">
			<h1>보호소 입양 공고</h1> // 保護施設の譲渡公告
			<p>구조 공고 기간이 종료되어 입양이 가능한 아이들입니다.</p> // 保護期間が終了し、譲渡可能となった子たちです。
		</header>

		<!-- JSONデータを非表示フィールドで渡す -->
		<input type="hidden" id="animalDetail"
			value='<c:out value="${animalDetail}"/>' />

		<div id="adoptLikes" hidden>${adoptLikes }</div>

		<div id="detail-container">
			<!-- JavaScriptがデータを動的に挿入する -->
		</div>

		<a href="javascript:history.back();" class="back-button"> 뒤로가기 </a>
	</div>

	<script>
		/* アイコンをクリックしたときにログイン状態を確認して「お気に入り」を切り替える関数 */
		function checklog(div) {
			// お気に入り数の値を取得
			let adoptLikesVal = document.getElementById('adoptLikes').innerText;
			// アイコン要素を取得
			let icon = div.firstElementChild;

			// アイコンのスタイルを切り替え（赤いハートのON/OFF）
			if (icon.classList.contains("fa-regular")) {
				icon.classList.remove("fa-regular");
				icon.classList.add("fa-solid", "red");
			} else {
				icon.classList.remove("fa-solid", "red");
				icon.classList.add("fa-regular");
			}

			// ログイン状態を確認するためのAjaxリクエスト
			var xhr = new XMLHttpRequest();
			xhr.open('GET', 'checkLogin', true);
			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4 && xhr.status === 200) {
					if (xhr.responseText === 'loggedIn') {
						// ログイン済みの場合、お気に入り登録を実行
						console.log(icon.dataset.val);
						likeSet(icon.dataset.val);
					} else if (xhr.responseText === 'notLoggedIn') {
						// 未ログインの場合、ログインページへ遷移
						window.location.href = 'loginC';
					}
				}
			};
			xhr.send();
		}

		/*「お気に入り」タブをクリックしたときの処理 */
		$(function() {
			$('#favoritesTab')
					.click(
							function() {
								// ログイン状態を確認するAjaxリクエスト
								var xhr = new XMLHttpRequest();
								xhr.open('GET', 'checkLogin', true);
								xhr.onreadystatechange = function() {
									if (xhr.readyState === 4
											&& xhr.status === 200) {
										if (xhr.responseText === 'loggedIn') {
											// ログイン済みの場合、「お気に入り」ページへ遷移
											window.location.href = 'AdoptionController?action=favorites';
										} else if (xhr.responseText === 'notLoggedIn') {
											// 未ログインの場合、ログインページへ遷移
											window.location.href = 'loginC';
										}
									}
								};
								xhr.send();
							});

		})
	</script>
</body>
</html>
