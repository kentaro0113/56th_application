package com.chibafes.chibafes56;

/**
 * Statics
 * Created by llrk on 2017/08/05.
 * 定数を記述するファイル
 */

public class Statics {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //この下の"http://demo.chibafes.com/appli/php/○○/～～～～.php"の○○は開催する回の序数(第55回→55th, 第57回→57th)を埋める！！

    static final String URL_UPDATE       = "https://lolipop-dp38028386.ssl-lolipop.jp/appli/php/57th/update_57th.php";
    static final String URL_ENQUETE      = "https://lolipop-dp38028386.ssl-lolipop.jp/appli/php/57th/enquete_answer_57th.php";
    static final String URL_CHIBAFES_WEB = "http://chibafes.com/";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //これ以降の文は変えない！

    static final int NONE                = -1;
    public static final int LIMIT_COUNT_NO      = 99999; // 各リストの最大index値（保険）

    static final int TYPE_SELECT         = 1; // アンケート：選択式
    static final int TYPE_TEXT           = 2; // アンケート：記述式

    static final int HAPPI_SERIF_NORMAL = 0;   // 通常時のセリフ
    static final int HAPPI_SERIF_TAP    = 1;   // タップした時のセリフ

    static final String IMAGE_PATH = "prcut/";
}
