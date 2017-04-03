/*************************************************************
 * Copyright(c) 2017 Japan Third Party Co.,Ltd.
 *
 * 機能説明 ： COMポート通信により機器にプリント命令を送信し、戻り値の数値部分を取得する
 * 作成者 : okino
 * 作成日 : 2017/3/6
 * バージョン : 1.0
 * 変更履歴 :
 ************************************************************/

package data.port_connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class PORT_Main {

  public static void main(String args[]) {

    // COMポート設定
    String COM_port_num = null;
    int bitrate = 0;
    String databit = "0";
    String stopbit = null;
    String partiy = null;
    String flowctrl = null;
    int serialPortDatabits = 0;
    int serialPortStopbits = 0;
    int serialPortPartiy = 0;
    int serialFlowControl = 0;
    try {
      // COMポート設定
      COM_port_num = "COM1";
      bitrate = 2400;
      databit = "8";
      stopbit = "1";
      partiy = "none";
      flowctrl = "none";

      switch (databit) {
        case "8":
          serialPortDatabits = SerialPort.DATABITS_8;
      }
      switch (stopbit) {
        case "1":
          serialPortStopbits = SerialPort.STOPBITS_1;
      }
      switch (partiy) {
        case "none":
          serialPortPartiy = SerialPort.PARITY_NONE;
      }
      switch (partiy) {
        case "none":
          serialPortPartiy = SerialPort.PARITY_NONE;
      }
      switch (flowctrl) {
        case "none":
          serialFlowControl = SerialPort.FLOWCONTROL_NONE;
      }

      if (args.length > 0) {
        COM_port_num = args[0];
      }

      CommPortIdentifier comID = CommPortIdentifier.getPortIdentifier(COM_port_num);

      // COMポートopen
      CommPort commPort = comID.open("port_connection", 2000);// open("ApplicationName",
                                                              // Timeout);
      SerialPort port = (SerialPort) commPort;
      // パラメータ設定
      port.setSerialPortParams(bitrate, serialPortDatabits, serialPortStopbits, serialPortPartiy);
      port.setFlowControlMode(serialFlowControl);

      // 受信ストリーム
      BufferedReader is = null;
      // 送信ストリーム
      PrintStream os = null;

      is = new BufferedReader(new InputStreamReader(port.getInputStream()));
      os = new PrintStream(port.getOutputStream(), true);

      // RXTX仕様
      port.disableReceiveTimeout();
      port.enableReceiveThreshold(1);

      // データの送信
      os.print("R, MD, 1");
      os.print("\r\n");

      String line = "";

      Thread.sleep(500);
      while (is.ready()) {
        Thread.sleep(500);
        System.out.println();
        // 送信先からの戻り値を受信
        line = is.readLine().toString();
        System.out.println(line);
        if (line == "END") {
          break;
        }
      }

      /*
       * line.trim();
       * String[] linex = line.split("g", 0);
       * String weight = linex[0];
       *
       * System.out.println(weight);
       */
      // 戻り値から数値を取り出す
      is.close();
      os.close();
      port.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }
}
