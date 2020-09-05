package com.chibafes.chibafes56;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

/**
 * Main Activity
 * Created by llrk on 2017/08/04.
 * 起動時に呼び出される画面
 */

public class MainActivity extends Activity implements HttpPostAsync.AsyncTaskCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーの表示設定：非表示にする
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // このActivityに関連づけるレイアウトの設定
        setContentView(R.layout.activity_main);

        String buf = Commons.readString(this, "lastdate");
        String paramString;
        if(buf == null) {
            paramString = "lastdate=0";
        }
        else {
            paramString = "lastdate=" + buf;
        }
        long nUserId = Commons.readLong(this);
        if(nUserId == Statics.NONE) {
            paramString = paramString + "&user_id=0";
        }
        else {
            paramString = paramString + "&user_id=" + nUserId;
        }

        TextView textProgress = findViewById(R.id.textProgress);
        textProgress.setText("通信中\n");

        // アンケートや情報などをネットワーク更新する処理を開始する
        HttpPostAsync postObject = new HttpPostAsync(this);
        postObject.execute(Statics.URL_UPDATE, paramString);
    }

    // ネットワーク更新前処理
    public void preExecute() {
    }
    // ネットワーク更新後処理
    public void postExecute(String result, boolean bError) {
        if(result != null && !bError) {
            // 正常にデータが取得できた場合、更新処理を行う
            String[] arrStr = result.split("\n");
            String[] arrayKeys = {"data_kikaku", "data_news", "data_enquete", "data_map"};

            for(int i = 1; i <= 4; ++i) {
                if(arrStr[i] != null && arrStr[i].length() > 0) {
                    String sBuf = "[" + arrStr[i].replace("}{", "},{") + "]";
                    Commons.writeString(this, arrayKeys[i - 1], sBuf);
                }
            }
            Commons.writeString(this, "lastdate", arrStr[0]);
            checkRunState();
        }
        else {
            // エラーが発生した旨のダイアログを表示
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.EnqueteErrorTItle))
                    .setMessage(getResources().getString(R.string.ConnectionError2))
                    .setPositiveButton(getResources().getString(R.string.ButtonOk), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkRunState();
                        }
                    })
                    .show();
        }
    }
    // 通信中の処理
    public void progressUpdate(int progress) {
    }
    // 通信キャンセル時の処理
    public void cancel() {
        checkRunState();
    }

    private void checkRunState() {
        // アプリインストール後の初回起動かどうかのチェックを行う
        if(Commons.readLong(this) == Statics.NONE) {
            // 初回起動なら初回起動用の画面へ遷移する
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            // 処理が終わったらこのActivityを破棄する
            finish();
        }
        else {
            // 初回起動でなければメインメニューへ遷移する
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            // 処理が終わったらこのActivityを破棄する
            finish();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // バックキー押下時の処理
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// バックキーを押しても何も起きないようにする
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}