package com.dangs.hy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 「お気に入り」登録・解除処理を行うコントローラー
@WebServlet("/AdoptionLikeC")
public class AdoptionLikeC extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			// DAOクラスを呼び出して、お気に入り登録の有無をチェック・更新
			AdoptionDAO.likeCheck(request,response);
	
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
