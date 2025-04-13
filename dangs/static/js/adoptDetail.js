let animal;
document.addEventListener('DOMContentLoaded', () => {

	const animalDetails = document.getElementById('animalDetail').value;
	const detailContainer = document.getElementById('detail-container');
	animal = JSON.parse(animalDetails);


	if (animalDetails) {

		try {
			// JSONデータをパースする

			// kindCdを変換：[犬]を[子犬]に置き換える
			let kindCd = animal.kindCd.replace('[개]', '[강아지]').trim();

			let age = "";

			if (animal.age) {
				const birthYearMatch = animal.age.match(/^(\d{4})/); // 生年を抽出する
				const additionalInfoMatch = animal.age.match(/\((.*?)\)/); // 追加情報（カッコ内の内容）を抽出する

				if (birthYearMatch) {
					const birthYear = parseInt(birthYearMatch[1], 10); // 生年をパースする
					const currentYear = new Date().getFullYear(); // 現在の年

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

			// 詳細情報をHTMLとして生成する
			const contentHtml = `
            	<div class="animalInfo">
            		<div class="animalDetailImg">
                		<img src="${animal.popfile}" alt="Animal Image" >
                	</div>
                	<div class="animalDetailInfo-container">
                		<div class="animalBasicInfo">
                			<div class="animal_title">
                				<img src="https://cdn-icons-png.flaticon.com/512/8771/8771588.png">
                				<div>${kindCd}</div>
                				<span> | </span>
                				<div>${age}</div>
                			</div>
                			<div class="like-button" onclick="checklog(this)" data-id="${animal.desertionNo}">
                				<i id="heart-icon" class="fa-regular fa-heart" aria-hidden="true"></i>
                			</div>
                		</div>
                		<div class="animalDetailInfo">
                			<div class="animal_info_low">	
                				<span>${animal.sexCd === 'F' ? '여아' : '남아'}</span>, 
                				<span>${animal.neuterYn === 'Y' ? '중성화 완료' : '중성화 미완료'}</span>
                			</div>
                			<div class="animal_info_low">	
                				<span>${animal.colorCd}</span>
                				<span>${animal.weight}</span>
                			</div>
                			<div class="animal_info_low">	
                				<span>특징:</span> <span> ${animal.specialMark}</span>
                			</div>
                		</div>
                		<div class="animalState">
	                		<div>상태 : ${animal.processState}</div>
	                		<span></span>
	                		<div>발견 장소: ${animal.happenPlace}</div>
                		</div>
                	</div>
                </div>
                <div class="shelterInfo">
                	<strong> 보호소 정보 </strong>
                	<p>보호소 이름: ${animal.careNm}</p>
                	<p>보호소 주소: ${animal.careAddr}</p>
                	<p>보호소 전화번호: ${animal.careTel}</p>
                </div>
            	
            `;

			// コンテナに詳細情報を挿入する
			detailContainer.innerHTML = contentHtml;
			likeSet(-1)
		} catch (error) {
			// JSONパースエラーの処理
			console.error('JSON 파싱 오류:', error);
			detailContainer.innerHTML = '<p>데이터를 처리하는 중 오류가 발생했습니다.</p>';
		}

	} else {
		// JSONデータが存在しない場合の処理
		detailContainer.innerHTML = '<p>데이터를 불러올 수 없습니다.</p>';
	}

});

/* 関心公告のチェック */
function likeSet(chk){
	fetch('AdoptionLikeC?adoptLikesVal=' + animal.desertionNo + '&chk='+chk, {
		method: "get",
		headers: {
			'Content-Type': 'application/json', // JSONデータを送信する際の設定
		}
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok ' + response.statusText);
			}
			return response.json(); // JSONレスポンスをパースする
		})
		.then(data => {
			console.log('GET 요청 데이터:', data); // レスポンスデータを利用する
				let icon = document.querySelector("#heart-icon");
			if(data == 1){
				icon.classList.remove("fa-regular");
		        icon.classList.add("fa-solid", "red");
		        icon.dataset.val = data;
			}
				icon.dataset.val = data;
		})
}