package data.tcp_connection;

/*********************************************************************************
 * Copyright(c) 2017 Japan Third Party Co.,Ltd.
 *
 * 機能説明 ： TCP/IP通信により機器に任意で命令を送信し、戻り値の必要部分を取得する
 * 引数 : ${IPアドレス} ${ポート番号} ${機器識別子} ${C_on:命令コマンド有}
 * 作成者 : okino
 * 作成日 : 2017/3/24
 * バージョン : 1.0
 * 変更履歴 :
 *********************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import java.text.SimpleDateFormat;

import javax.swing.*;

// original package import
import data.char_proc.REC_DATA_PROC;
import data.command_setting.CMD_ST;

public class TCP_Main {
  /*
   * ログ出力設定
   * static final Logger logger = Logger.getLogger(Main.class.getName());
   * static final String filePath = "log/"; // ログファイルパス
   *
   * logger.finest("");
   * logger.finer("");
   * logger.fine("");
   * logger.config("");
   * logger.info("");
   * logger.warning("");
   * logger.severe("");
   */

  public static void main(String[] args) {
    String test[] = { "169.254.128.2", "5000", "TEST", "false" };
    String HRB001[] = { "169.254.128.2", "5000", "HRB001", "true" };
    String HRB002[] = { "169.254.128.2", "5000", "HRB002", "true" };
    String MTL001[] = { "169.254.128.2", "5000", "MTL001", "true" };
    String MTL002[] = { "169.254.128.2", "5000", "MTL002", "true" };
    String MTL003[] = { "169.254.128.2", "5000", "MTL003", "true" };
    String MTL004[] = { "169.254.128.2", "5000", "MTL003", "true" };
    String IJE001[] = { "169.254.128.2", "5000", "IJE001", "true" };
    String KEM001[] = { "169.254.128.2", "5000", "KEM001", "true" };
    String AVI001[] = { "169.254.128.2", "5000", "AVI001", "false" };
    String CAP001[] = { "169.254.128.2", "5000", "CAP001", "false" };

    tcpMain(MTL004);
  }

  public static void tcpMain(String[] args) {

    try {
      /*********** argument setting *************/
      // デバッグフラグ
      boolean debug_flg = false;
      debug_flg = true;
      // 取得行カウント
      int rowNum = 1;
      // IPアドレス設定
      InetAddress IP_adress = null;// InetAddress.getByName("169.254.128.2");
      if (args.length > 0) {
        IP_adress = InetAddress.getByName(args[0]);
      }
      // ポート番号設定
      int port_num = 5000;
      if (args.length > 1) {
        port_num = Integer.parseInt(args[1]);
      }
      // 測定対象指定
      String target = "TEST";
      if (args.length > 2) {
        target = args[2];
      }
      // 命令コマンド送信の有無
      boolean cmd_flg = false;
      if (args.length > 3) {
        cmd_flg = Boolean.parseBoolean(args[3]);
      }
      // cmd_flg = true;

      // ソケットアドレス
      SocketAddress socket_add = null;
      // クライアントソケット
      Socket socket = new Socket();
      // 受信ストリーム
      BufferedReader in = null;
      // 送信ストリーム
      BufferedWriter out = null;
      // 受信文字列
      String line = null;
      boolean output_flg = false;

      /*********** Communication processing ************/
      System.out.println("Communication processing");
      if (debug_flg) {
        IP_adress = InetAddress.getByName("127.0.0.1");
        port_num = 1024;
      }
      InetSocketAddress endpoint = new InetSocketAddress(IP_adress, port_num);
      // クライアントソケット接続
      socket.connect(endpoint, 1000);

      // 受信ストリーム
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // 送信ストリーム
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      // output file
      String char_code = "";
      char_code = "us-ascii";
      // char_code = "utf-8";
      // char_code = "shift-jis";
      // char_code = "Windows-31j";
      // char_code = "utf-16";
      // char_code = "jis";

      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
      File newfile = new File(".\\output", target + "_" + sdf.format(date) + ".txt");
      PrintWriter pw = new PrintWriter(
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newfile), char_code)));
      JFrame frame = new JFrame();
      int ans = 0;

      // output file 作成
      newfile.createNewFile();

      // 命令コード送信
      if (cmd_flg) {
        CMD_ST cmd_st = new CMD_ST();
        System.out.println("command setting");
        String cmd = cmd_st.select_cmd(target);// コマンド選定
        out.write(cmd); // コマンドをソケット書き込む
        out.newLine(); // 改行
        System.out.println("command flush...");
        out.flush(); // コマンド送信
        System.out.println("cmd : " + cmd);
      }

      // 受信データ処理準備
      REC_DATA_PROC rec_d_p = new REC_DATA_PROC();
      // 戻り値取得
      ArrayList<String> lines = new ArrayList<String>();

      System.out.println("*************  Start REC  *************");

      while (true) {
        if (in.ready()) {
          line = in.readLine();
          System.out.println("line (" + rowNum + ") : " + line);
          line = rec_d_p.ch_char_code(line, target);// 文字コード変換
          if (checkBeforeWritefile(newfile)) {
            pw.println(line);
          }
          line = line.trim();
          lines.add(line);
          rowNum++;
          output_flg = true;
        } else if (output_flg) {
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setAlwaysOnTop(true);
          // 確認ダイアログ表示
          ans = JOptionPane.showConfirmDialog(frame, "処理を続行しますか？", "取得処理", JOptionPane.YES_NO_OPTION);
          if (ans == 0) {// yes
            output_flg = false;
          } else {// no
            break;
          }
        }
      }
      System.out.println("lines : " + lines);
      System.out.println("*************  End REC  *************");

      // 受信文字列処理
      String[] res = rec_d_p.conv(lines, target);
      System.out.println("res : " + Arrays.toString(res));
      /*********** End processing **************/
      out.close();
      in.close();
      pw.close();
      socket.close();
      frame.dispose();
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean checkBeforeWritefile(File file) {
    if (file.exists()) {
      if (file.isFile() && file.canWrite()) {
        return true;
      }
    }
    return false;
  }
}
