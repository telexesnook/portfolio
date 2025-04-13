$(function() {

	// 現在の年を計算する
	const currentYear = new Date().getFullYear();

	// ページ設定
	let currentPage = 1; 
	const totalPages = 4; 

	/* ページネーションボタンを生成する */
	function renderPagination() {
		const paginationContainer = $('#pagination');
		paginationContainer.empty(); // 既存のボタンを初期化する

		for (let i = 1; i <= totalPages; i++) {
			const pageButton = $('<button>')
				.addClass('pagination-number')
				.text(i)
				.data('page', i); // ページ番号を保存する
			if (i === currentPage) {
				pageButton.addClass('active'); // 現在のページを強調表示する
			
			}

			paginationContainer.append(pageButton);
		}
	}

	/* APIデータの表示する */
	function loadPageData(pageNo) {
		$.ajax({
			url: 'AjaxController',
			type: 'GET',
			data: { pageNo: pageNo },
			dataType: 'json',
			success: function(resData) {
				console.log(resData);

				const items = resData.response.body.items.item;

				// 結果を初期化する
				$('#adoptPosts').empty();

				// 各項目をループしてHTMLを生成する
				$.each(items, function(i, ob) {

					// 性別を変換する（F = メス、M = オス、不明）
					let gender = (ob.sexCd === 'F') ? '여아' : (ob.sexCd === 'M') ? '남아' : '알 수 없음';

					// 去勢・避妊の有無を変換する（Y = 済、N = 未、不明）
					let neuterStatus = (ob.neuterYn === 'Y') ? '중성화 완료' : (ob.neuterYn === 'N') ? '중성화 미완료' : '알 수 없음';

					// kindCdを変換：[犬]を[子犬]に置き換える
					let kindCd = ob.kindCd.replace('[개]', '[강아지]').trim();

					// 年齢を計算する
					let age = '';
					if (ob.age) {
						const birthYearMatch = ob.age.match(/^(\d{4})/); // 生年（4桁の数字）を抽出する
						const additionalInfoMatch = ob.age.match(/\((.*?)\)/); // 追加情報（カッコ内の内容）を抽出する

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
                        <div class="item" data-id="${ob.desertionNo}">
                            <img src="${ob.popfile}" alt="Thumbnail" class="detail-image">
							<div class="itme-content">                            
                           		<p class="detail-kindCd"><strong> ${kindCd}</strong> </p>
                           		<p> ${age} </p>
                            	<p> ${gender} / ${neuterStatus} </p>
                            	<p> 지역: ${ob.happenPlace}</p>
                        	</div>
                        </div>
                    `;

					$('#adoptPosts').append(itemHtml);
				});

				// ページネーションを更新する
				renderPagination();

			},
			error: function(xhr, status, error) {
				console.log('에러 발생');
				console.log('xhr:', xhr);
				console.log('status:', status);
				console.log('error:', error);
				$('#adoptPosts').html('<p>데이터를 불러오는데 실패했습니다.</p>');
			}
		});
	}

	/* 初期データの読み込み */
	loadPageData(currentPage);

	/* クリック時に詳細ページへ遷移する */
	$(document).on('click', '.item', function() {
		const desertionNo = $(this).data('id');
		window.location.href = `AdoptDetailController?desertionNo=${desertionNo}`;
	});

	
	/* ページ移動 */ 
	//「前へ」ボタンをクリックする
	$('#prevPage').click(function() {
		if (currentPage > 1) {
			currentPage--;
			loadPageData(currentPage);
		} else {
			alert('첫 페이지입니다.'); // 最初のページです。
		}
	});

	//「次へ」ボタンをクリックする 
	$('#nextPage').click(function() {
		if (currentPage < totalPages) {
			currentPage++;
			loadPageData(currentPage);
		} else {
			alert('마지막 페이지입니다.'); // 最後のページです。
		}
	});

	// ページ番号のクリックイベント
	$(document).on('click', '.pagination-number', function() {
		const selectedPage = $(this).data('page');
		currentPage = selectedPage;
		loadPageData(currentPage);
	});

});

