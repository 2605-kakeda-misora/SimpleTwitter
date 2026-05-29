package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		// 編集画面を表示するための「id」を受け取る
		String id = request.getParameter("id");
		List<String> errorMessages = new ArrayList<>();
		Message editMessage = null;

		// idがないときと数字以外のときまとめる
		if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
			errorMessages.add("不正なパラメータが入力されました。");
		} else {
			// パースした数字（int）を用意する
			int parsedId = Integer.parseInt(id);

			// 数字（parsedId）をそのまま Service に渡すだけ
			editMessage = new MessageService().select(parsedId);


			// データの存在とユーザーIDのチェック
			if (editMessage == null || editMessage.getUserId() == 0) {
				errorMessages.add("不正なパラメータが入力されました。");
			}
		}

		// エラーメッセージがある場合は、トップ画面へリダイレクト
		if (!errorMessages.isEmpty()) {
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		// 編集画面から送られてきたidと更新されたつぶやきを受け取る
		String id = request.getParameter("id");
		List<String> errorMessages = new ArrayList<String>();
		String text = request.getParameter("text");

		Message editMessage = new Message();
		int parsedId = Integer.parseInt(id);
		editMessage.setId(parsedId);
		editMessage.setText(text);

		// バリデーションチェック
		if (!isValid(text, errorMessages)) {
			// エラー時はメッセージをリクエストスコープに格納
			request.setAttribute("errorMessages", errorMessages);

			// すでに値がセットされているeditMessageをそのままリクエストに格納
			request.setAttribute("editMessage", editMessage);

			request.getRequestDispatcher("/edit.jsp").forward(request, response);
			return;
		}
		// データベースの更新処理を実行
		new MessageService().update(editMessage);

		// 更新が終わったら、ホーム画面にリダイレクト
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		if (StringUtils.isBlank(text)) {
			errorMessages.add("入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
