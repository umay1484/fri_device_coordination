package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbAccess {
  public static void main(String[] args) {
    DbAccess dbAccess = new DbAccess();

    try {
      dbAccess.selectOracle();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void selectOracle() throws Exception {
    // ユーザ名
    String user = "FRI-PJ";
    // パスワード
    String pass = "admin";
    // サーバ名
    String servername = "localhost";
    // SID
    String sid = "xe";

    Connection conn = null;
    Statement stmt = null;
    ResultSet rset = null;

    try {
      // JBBCドライバクラスのロード
      Class.forName("oracle.jdbc.driver.OracleDriver");

      // Connectionの作成
      conn = DriverManager.getConnection("jdbc:oracle:thin:@" + servername + ":1521:" + sid, user, pass);

      // Statementの作成
      stmt = conn.createStatement();

      // Resultsetの作成
      rset = stmt.executeQuery(
          "select gu.GROUP_NAME, ug.USER_NAME from USER_GROUP ug left join GROUP_NAME gu on ug.GROUP_ID = gu.GROUP_ID order by ug.USER_NAME");

      // 取得したデータを出力する
      while (rset.next()) {
        System.out.println(rset.getString("GROUP_NAME") + "," + rset.getString("USER_NAME"));
      }

    } catch (ClassNotFoundException e) {
      throw e;
    } catch (SQLException e) {
      throw e;
    } catch (Throwable e) {
      throw e;
    } finally {
      try {
        /* クローズ処理 */
        if (rset != null) {
          rset.close();
          rset = null;
        }

        if (stmt != null) {
          stmt.close();
          stmt = null;
        }

        if (conn != null) {
          conn.close();
          conn = null;
        }
      } catch (Throwable e) {
        // nop
      }
    }
  }
}
