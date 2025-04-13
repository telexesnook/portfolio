package com.dangs.hy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dangs.sw.UserDTO;
import com.google.gson.JsonArray;

// ログインユーザーのお気に入り一覧を取得してJSONで返すコントローラー
@WebServlet("/GetFavController")
public class GetFavController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// セッションからユーザー情報を取得
		HttpSession hs = request.getSession();
		UserDTO user = (UserDTO) hs.getAttribute("user");
		
		// ログインしているユーザーのIDを取得
        String userId = user.getId(); 

		// 該当ユーザーのお気に入り公告を取得（JSON配列形式）
        JsonArray favorites = AdoptionDAO.getAdao().getUserFavorites(userId);

        // JSON形式でクライアントに返す
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(favorites.toString());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
