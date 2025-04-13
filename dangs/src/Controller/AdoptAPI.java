package com.dangs.hy;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AdoptAPI")
public class AdoptAPI extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		
		try {
	            // 外部のAPIからすべてのページの譲渡情報を取得し、データベースに保存する
	            AdoptionDAO.getAdao().fetchAndSaveAllPages();

	            // 保存処理が正常に完了したことを示すメッセージをJSON形式でクライアントに返す
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write("{\"message\":\"Data saved successfully.\"}");
	        } catch (Exception e) {
				// エラーが発生した場合は、エラーメッセージを返す
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            response.getWriter().write("{\"message\":\"Error occurred while saving data.\"}");
	        }
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}
