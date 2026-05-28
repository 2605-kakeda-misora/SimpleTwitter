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

		// IDが正常に取得できたら削除を実行する
		if (id != null && !id.isEmpty()) {

			Message messageId = new Message();

			// 文字列のIDを、MessageServiceが求めている型（Message型オブジェクト）に合わせるため、
			// 一度数値(int)に変換してからmessageIdオブジェクトにセットします
			int parsedId = Integer.parseInt(id);
			messageId.setId(parsedId);

			//  MessageServiceの削除メソッドを呼び出す
			new MessageService().delete(messageId);
		}

		// ホーム画面にリダイレクト
		response.sendRedirect("./");
	}
}
