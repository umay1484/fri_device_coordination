package data.char_proc;

/*************************************************************
 * Copyright(c) 2017 Japan Third Party Co.,Ltd.
 *
 * 機能説明 ： 受信データを検査機器ごとに最適な処理する
 * 作成者 : okino
 * 作成日 : 2017/3/24
 * バージョン : 1.0
 * 変更履歴 :
 ************************************************************/

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.xml.bind.DatatypeConverter;

public class REC_DATA_PROC {
  private String[] res;

  public static void main(String[] args) {

  }

  /***** 受信データを検査機器ごとの最適な文字コードに変換する( line : 行単位受信データ, type : 機器識別子 ) ******/
  public String ch_char_code(String line, String type) throws UnsupportedEncodingException {
    String char_code = null;
    String eucStr = null;
    switch (type) {
      case "IJE001":// B-505
        char_code = "utf-8";
        break;
      case "HRB001":// F-52
      case "HRB002":// D-52
      case "MTL001":// XS205
      case "MTL002":// PL1502-S
      case "MTL003":// XPE32001LV
      case "MTL004":// ME204
      case "AVI001":// 3250 Osmometer
      case "CAP001":// CRC-55tR
        char_code = "us-ascii";
        break;
      case "KEM001":// AT-610
        char_code = "Windows-31j";
        break;
      case "TEST":// PHメータ
        String char_code_list[] = { "utf-8", "utf-16", "shift-jis", "Windows-31j",
            "iso-8859-1", "euc-jp", "iso2022jp", "us-ascii", "jis" };
        byte[] byteList = line.getBytes();
        String hexString = DatatypeConverter.printHexBinary(byteList);
        System.out.println("hexString : " + hexString);
        System.out.println("Default Charset encoding:");
        for (int i = 0; i < char_code_list.length; i++) {
          eucStr = new String(line.getBytes(char_code_list[i]), char_code_list[i]);
          System.out.print(char_code_list[i] + " : " + eucStr + ",");
        }
        System.out.println();
        char_code = "utf-8";
        break;
    }
    eucStr = new String(line.getBytes(char_code), char_code);
    return eucStr;
  }

  /**** 受信データから検査機器ごとに必要な部分を抽出する( data : 全受信データ, type : 機器識別子 ) ******/
  public String[] conv(ArrayList<String> list, String type) {
    res = null;
    String[] res_temp = null;
    ArrayList<String> data_temp = new ArrayList<String>();
    list.removeAll(Collections.singleton("")); // 空文字削除
    // list.removeAll(Collections.singleton(null)); // null削除
    list.trimToSize();// null削除 listサイズ調整

    res_temp = (String[]) list.toArray(new String[0]);// リストを配列に変換

    System.out.println("res_temp : " + Arrays.toString(res_temp));
    switch (type) {
      case "MTL001":// XS205
      case "MTL002":// PL1502-S
      case "MTL003":// XPE32001LV
      case "MTL004":// ME204
        for (String tmp : res_temp) {
          data_temp.add(tmp.trim());
        }
        // data_temp.add(res_temp[0].trim().split("g", 0).toString());
        break;
      case "HRB001":// F-52
      case "HRB002":// D-52
        data_temp.add(res_temp[13]); // ph値
        data_temp.add(res_temp[17]); // 温度
        break;
      case "KEM001":// AT-610
        for (String tmp : res_temp) {
          data_temp.add(tmp.trim());
        }
        break;
      case "IJE001":// B-505
        for (String tmp : res_temp) {
          data_temp.add(tmp.trim());
        }
        break;
      case "AVI001":// 3250 Osmometer
        for (String tmp : res_temp) {
          data_temp.add(tmp.trim());
        }
        break;
      case "CAP001":// CRC-55tR
        for (String tmp : res_temp) {
          data_temp.add(tmp.trim());
        }
        break;
      case "TEST":
        for (String tmp : res_temp) {
          data_temp.add(tmp.trim());
        }
        break;
    }
    res = (String[]) data_temp.toArray(new String[0]);// リストを配列に変換
    return res;
  }
}
