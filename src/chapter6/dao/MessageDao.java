package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public void insert(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // text
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, Message id) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM messages WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE, new Object() {
					}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
					throw new SQLRuntimeException(e);
				} finally {
					close(ps);
				}
			}
		}

	}

	public Message select(Connection connection, Message id) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;

		try {
			// 編集するつぶやき1件だけ取得するためのSQL文
			String sql = "SELECT * FROM messages WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id.getId()); // 引数で届いたMessageオブジェクトからIDを取り出してセット

			// SQLを実行し、結果（ResultSet）を受け取る
			java.sql.ResultSet rs = ps.executeQuery();

			Message message = null;
			// 該当するデータが見つかった場合のみ、Messageオブジェクトに移し替える
			if (rs.next()) {
				message = new Message();
				message.setId(rs.getInt("id"));
				message.setUserId(rs.getInt("user_id"));
				message.setText(rs.getString("text"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setUpdatedDate(rs.getTimestamp("updated_date"));
			}

			// 取得した1件のメッセージ（見つからなければnull）を返す
			return message;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE, new Object() {
					}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
					throw new SQLRuntimeException(e);
				} finally {
					close(ps);
				}
			}
		}
	}

	public void update(Connection connection, Message id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE messages SET ");
			sql.append("    text = ?, "); // 新しいつぶやきの本文
			sql.append("    updated_date = CURRENT_TIMESTAMP "); // 更新日時を現在の時刻にする
			sql.append("WHERE id = ?"); // 編集したいつぶやきのID

			ps = connection.prepareStatement(sql.toString());

			// 引数で届いた「Message id」オブジェクトから中身を取り出してSQLにセット
			ps.setString(1, id.getText()); // 新しく入力されたテキストをセット
			ps.setInt(2, id.getId()); // 編集したいつぶやきのIDをセット

			// SQLを実行
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE, new Object() {
					}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
					throw new SQLRuntimeException(e);
				} finally {
					close(ps);
				}
			}
		}
	}

}
