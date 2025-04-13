package com.dangs.hy;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 譲渡情報ページの表示を制御するコントローラー
@WebServlet("/AdoptionController")
public class AdoptionController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// クエリパラメータ "action" を取得（favorites など）
		String action = request.getParameter("action");
		String contentPage;

		// 「お気に入り」譲渡情報を表示する場合
		if ("favorites".equals(action)) {
		
			contentPage = "jsp/hy/myFavoritesAdopt.jsp"; // お気に入り公告ページ
	
		// それ以外は通常の保護施設譲渡公告ページを表示
		} else {
		
			contentPage = "jsp/hy/shelterAdoptions.jsp"; // 保護施設公告ページ
	
		}

		// 表示するコンテンツページをリクエストに設定
		request.setAttribute("content", contentPage);
		// 非ログインテンプレートにフォワード
		request.getRequestDispatcher("noLoginIndex.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
