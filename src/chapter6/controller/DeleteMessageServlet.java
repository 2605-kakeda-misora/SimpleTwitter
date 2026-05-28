package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/deleteMessage" })
public class DeleteMessageServlet extends HttpServlet {
	/**
	    * ロガーインスタンスの生成
	    */
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public DeleteMessageServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		// JSPから送られてきたつぶやきIDを受け取る
		String id = request.getParameter("id");

		Message messageId = new Message();
		//箱用意せず型変換で渡す
		messageId.setId(Integer.parseInt(id));

		//  MessageServiceの削除メソッドを呼び出す
		new MessageService().delete(messageId);

		// ホーム画面にリダイレクト
		response.sendRedirect("./");
	}
}
