package com.dangs.hy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 動物の詳細ページに遷移するコントローラー
@WebServlet("/AdoptDetailController")
public class AdoptDetailController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// URLパラメータから個体識別番号（desertionNo）を取得
		String desertionNo = request.getParameter("desertionNo");
		
		// 識別番号を元に詳細データ（JSON）を取得
		String animalDetail = AdoptionDAO.getAdao().getAnimalDetail(desertionNo);
		
		// 「いいね」機能用に個体識別番号をセット
		request.setAttribute("adoptLikes", desertionNo);

		// 動物の詳細データ（JSON文字列）をリクエスト属性に追加
		request.setAttribute("animalDetail", animalDetail);
		
		// 表示するJSPページのパスを指定
		request.setAttribute("content", "jsp/hy/adoptDetail.jsp");
		
		// 非ログインユーザー向けのテンプレートにフォワード
		request.getRequestDispatcher("noLoginIndex.jsp").forward(request, response);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
