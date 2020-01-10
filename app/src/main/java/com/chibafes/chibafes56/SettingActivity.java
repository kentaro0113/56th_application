package com.chibafes.chibafes56;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/**
 * Created by aki09 on 2017/09/01.
 */

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーの表示設定：非表示にする
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // このActivityに関連づけるレイアウトの設定
        setContentView(R.layout.activity_setting);
    }

    public void onClickButtonBack(View view) {
        // この画面を閉じる
        finish();
    }

    public void onClickButtonAbout(View view){
        new AlertDialog.Builder(this)
                    .setTitle(null)
                    .setMessage(getResources().getString(R.string.AboutCopyright))
                    .setPositiveButton(getResources().getString(R.string.ButtonOk),null)
                    .show();
    }
    public void onClickButtonToWeb(View view) {
        // Webサイトへ遷移する
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Statics.URL_CHIBAFES_WEB));
        startActivity(intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // バックキー押下時の処理
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 設定画面を閉じる
                finish();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
