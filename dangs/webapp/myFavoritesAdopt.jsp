<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/hy/favorites.css">
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous"></script>
<script type="text/javascript" src="js/hy/favoriteAdopt.js"></script>
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
		<div class="posts-section">
			<div id="favoritesList" class="posts-grid">
				<!-- APIデータの表示 -->
			</div>
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