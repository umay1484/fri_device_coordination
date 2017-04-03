package data.command_setting;

/*************************************************************
 * Copyright(c) 2017 Japan Third Party Co.,Ltd.
 *
 * 機能説明 ： 検査機器ごとに対応した命令コマンドを取得する
 * 作成者 : okino
 * 作成日 : 2017/3/24
 * バージョン : 1.0
 * 変更履歴 :
 ************************************************************/

public class CMD_ST {
  String cmd = null;

  /*** 機器ごとに対応した命令コマンドを取得( type : 機器識別子 ) ***/
  public String select_cmd(String type) {
    switch (type) {
      case "MTL001":// XS205
        cmd = "P101";
        break;
      case "MTL002":// PL1502-S
      case "MTL003":// XPE32001LV
      case "MTL004":// ME204
        cmd = "S";
        break;
      case "HRB001":// F-52
      case "HRB002":// D-52
        cmd = "R, MD, 1";
        break;
      case "KEM001":// AT-610
        cmd = "A";
        break;
      case "IJE001":// B-505
        cmd = "MD";
        break;
      case "AVI001":// 3250 Osmometer
        cmd = "";
        break;
      case "CAP001":// CRC-55tR
        cmd = "";
        break;
      case "P3":// F-52, DS-52
        cmd = "C, OL, 0";// 0 ONline 1: off
        break;
      case "TEST":
        cmd = "";
        break;
    }
    return cmd;
  }
}
