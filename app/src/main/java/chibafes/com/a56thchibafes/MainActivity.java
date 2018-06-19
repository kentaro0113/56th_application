package chibafes.com.a56thchibafes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {

    //プリファレンス
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // スプラッシュ画面から、2秒（2000ミリ秒)後に遷移する。
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {

                //プリファレンスの準備
                preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
                editor = preference.edit();

                if (!preference.getBoolean("Launched", false)) {
                    // 初回起動時の処理
                    // 初回起動なら初回起動用の画面へ遷移する
                    Intent intent = new Intent(MainActivity.this, FirstRunActivity.class);
                    startActivity(intent);
                    // 処理が終わったらこのActivityを破棄する
                    finish();
                    // プリファレンスの書き変え
                    editor.putBoolean("Launched", true);
                    editor.apply();
                } else {
                    // 二回目以降の処理
                    // 初回起動でなければメインメニューへ遷移する
                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    // 処理が終わったらこのActivityを破棄する
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                // バックキー押下時の処理
                case KeyEvent.KEYCODE_BACK:
                    // バックキーを押しても何も起きないようにする
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
