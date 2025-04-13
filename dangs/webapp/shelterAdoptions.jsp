<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="C"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/hy/adopt.css">
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous"></script>
<script type="text/javascript" src="js/hy/adopt.js"></script>
</head>
<body>

	<!-- ナビゲーションバー -->
	<nav class="navigation-bar">
		<ul class="nav-links">
			<li><a href="AdoptionController?action=shelter">보호소 입양 공고</a></li> // 保護施設の譲渡公告
			<li><a href="#" id="favoritesTab">관심있는 공고</a></li> // 気になる公告
		</ul>
	</nav>

	<div class="main-container">
		<!-- ページヘッダー -->
		<header class="page-header">
			<h1>보호소 입양 공고</h1> // 保護施設の譲渡公告
			<p>구조 공고 기간이 종료되어 입양이 가능한 아이들입니다.</p> // 保護期間が終了し、譲渡可能となった子たちです。
		</header>

		<!-- 一覧セクション -->
		<div class="posts-section">
			<div id="adoptPosts" class="posts-grid">
				<!-- APIデータの表示 -->
			</div>
		</div>

		<div class="pagination">
			<button id="prevPage" class="pagination-button">이전</button> // 前へ
			<div id="pagination" class="pagination-numbers"></div>
			<!-- ページ番号コンテナ -->
			<button id="nextPage" class="pagination-button">다음</button> // 次へ
		</div>
	</div>

	<script type="text/javascript">
		/*「お気に入り」タブをクリックしたときの処理 */
		$(function() {
			$('#favoritesTab')
					.click(
							function() {
								// ログイン状態を確認するためのAjaxリクエストを送信
								var xhr = new XMLHttpRequest();
								xhr.open('GET', 'checkLogin', true);
								xhr.onreadystatechange = function() {
									if (xhr.readyState === 4
											&& xhr.status === 200) {
										
										// ログイン済みの場合、「お気に入り公告」ページへ遷移
										if (xhr.responseText === 'loggedIn') {
											window.location.href = 'AdoptionController?action=favorites';
										
										// 未ログインの場合、ログインページへリダイレクト
										} else if (xhr.responseText === 'notLoggedIn') {
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