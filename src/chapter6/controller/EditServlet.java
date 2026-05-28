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

		// 3つのエラーパターンをcheckIdメソッドでチェック
		Message editMessage = checkId(id, errorMessages);

		// エラーメッセージがある場合は、トップ画面へリダイレクト
		if (!errorMessages.isEmpty()) {
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}

	private Message checkId(String id, List<String> errorMessages) {
		// IDがないとき
		if (id == null || id.isEmpty()) {
			errorMessages.add("不正なパラメータが入力されました。");
			return null;
		}

		int parsedId;
		try {
			// IDが数字以外のとき
			parsedId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			errorMessages.add("不正なパラメータが入力されました。");
			return null;
		}

		Message messageId = new Message();
		messageId.setId(parsedId);

		// データベースから対象のメッセージを取得
		Message editMessage = new MessageService().select(messageId);

		// idが空っぽのとき
		if (editMessage == null || editMessage.getUserId() == 0) {
			errorMessages.add("不正なパラメータが入力されました。");
			return null;
		}

		return editMessage;
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

		if (id != null && !id.isEmpty() && text != null) {
			Message messageId = new Message();
			int parsedId = Integer.parseInt(id);
			messageId.setId(parsedId);
			if (!isValid(text, errorMessages)) {
				// エラー時はメッセージをリクエストスコープに格納
				request.setAttribute("errorMessages", errorMessages);

				// 画面に入力内容を保持させるため、Messageオブジェクトを作成
				Message editMessage = new Message();
				//Messageオブジェクトにtextとidをセット
				editMessage.setId(parsedId);
				editMessage.setText(text);
				request.setAttribute("editMessage", editMessage);

				request.getRequestDispatcher("/edit.jsp").forward(request, response);
				return;
			}
			Message message = new Message();
			message.setText(text);
			messageId.setText(text);
			// データベースの更新処理を実行
			new MessageService().update(messageId);
		}
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
