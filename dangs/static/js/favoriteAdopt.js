$(document).ready(function() {

	let currentPage = 1; 


	/* サーバーからデータを取得する */
	function loadFavorites() {
		$.ajax({
			url: 'GetFavController', // 関心公告データを取得するためのコントローラー
			type: 'GET',
			dataType: 'json', // サーバーからJSON形式のデータを返す
			success: function(data) {
				console.log("관심 공고 데이터:", data);

				// 既存のデータを初期化する
				$('#favoritesList').empty();

				if (data.length === 0) {
					$('#favoritesList').html('<p>관심 등록된 데이터가 없습니다.</p>');
					return;
				}

				// 各項目をループしてHTMLを生成する
				data.forEach(item => {

					// 性別を変換する（F = メス、M = オス、不明）
					let gender = (item.sexCd === 'F') ? '여아' : (item.sexCd === 'M') ? '남아' : '알 수 없음';

					// 去勢・避妊の有無を変換する（Y = 済、N = 未、不明）
					let neuterStatus = (item.neuterYn === 'Y') ? '중성화 완료' : (item.neuterYn === 'N') ? '중성화 미완료' : '알 수 없음';

					// kindCdを変換：[犬]を[子犬]に置き換える
					let kindCd = item.kindCd.replace('[개]', '[강아지]').trim();

					// 年齢を計算する
					let age = '';
					if (item.age) {
						const birthYearMatch = item.age.match(/^(\d{4})/); // 生年（4桁の数字）を抽出する
						const additionalInfoMatch = item.age.match(/\((.*?)\)/); // 追加情報（カッコ内の内容）を抽出する

						if (birthYearMatch) {
							const birthYear = parseInt(birthYearMatch[1], 10); // 2025
							if (currentYear - birthYear > 0) {
								age = `${currentYear - birthYear}살`;
							} else if (additionalInfoMatch && additionalInfoMatch[1].includes('60일미만')) {
								age = '2개월 미만';
							} else {
								age = '알 수 없음';
							}
						} else {
							age = '알 수 없음';
						}
					} else {
						age = '알 수 없음';
					}

					// HTML
					const itemHtml = `
                 		  <div class="item favorite-item" data-id="${item.desertionNo}">
                            <img src="${item.popfile}" alt="Thumbnail" class="detail-image">
							<div class="itme-content">                            
                           		<p class="detail-kindCd"><strong> ${kindCd}</strong> </p>
                           		<p> ${age} </p>
                            	<p> ${gender} / ${neuterStatus} </p>
                            	<p> 지역: ${item.happenPlace}</p>
                        	</div>
                        </div>
                    `;
					$('#favoritesList').append(itemHtml);
				});
			},
			error: function(xhr, status, error) {
				console.error("관심 공고 데이터 로드 실패:", { xhr, status, error });
				$('#favoritesList').html('<p>데이터를 불러오는데 실패했습니다.</p>');
			}
		});
	}

	// 関心公告データを読み込む
	loadFavorites();

	/* クリック時に詳細ページへ遷移する */
	$(document).on('click', '.favorite-item', function() {
		const desertionNo = $(this).data('id');
		window.location.href = `AdoptDetailController?desertionNo=${desertionNo}`;
	});
});
