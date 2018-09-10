package com.chibafes.chibafes56;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by llrk on 2017/10/30.
 */

public class KikakuInfoActivity extends Activity {

    private static final String URL_COSYPLAY = "https://docs.google.com/forms/d/e/1FAIpQLSc96Yydes0IUX2ytCchbEP8iKBonznZe-ji1tCpp8tA1LvhtA/viewform";
    private static final String URL_COSYPLAY2 = "https://goo.gl/forms/8VOi0hHk06mqnkT43";
    private static final String URL_GRANDPRIX = "https://goo.gl/forms/AxdFYu1JIlHr5o9F2";
    private static final String URL_HAPPI_LINE = "https://store.line.me/stickershop/product/1169796/";
    private static final String URL_KIKAKU_OTHER1 = "https://twitter.com/chibafes_happi";
    private static final String URL_KIKAKU_OTHER2 = "https://twitter.com/share?hashtags=はっぴを探せ";
    private static final String URL_KIKAKU_OTHER3 = "https://twitter.com/ChibafesShinkan";
    private static final String URL_KIKAKU_OTHER4 = "https://twitter.com/share?hashtags=千葉大祭フォトコン";
    private static final String URL_KIKAKU_GUEST1 = "http://guest.chibafes.com/";
    private static final String URL_KIKAKU_GUEST2 = "https://twitter.com/ChibafesShinkan/status/905402689286057984";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーの表示設定：非表示にする
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // このActivityに関連づけるレイアウトの設定
        setContentView(R.layout.activity_kikakuinfo);

        int nVisiblePage = getIntent().getExtras().getInt("page");
        LinearLayout view1 = (LinearLayout) findViewById(R.id.viewInfo1);
        LinearLayout view2 = (LinearLayout) findViewById(R.id.viewInfo2);
        LinearLayout view3 = (LinearLayout) findViewById(R.id.viewInfo3);
        LinearLayout view4 = (LinearLayout) findViewById(R.id.viewInfo4);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        switch(nVisiblePage) {
            case 1:
                view1.setVisibility(View.VISIBLE);
                break;
            case 2:
                view2.setVisibility(View.VISIBLE);
                break;
            case 3:
                view3.setVisibility(View.VISIBLE);
                break;
            case 4:
                view4.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onClickButtonBack(View view) {
        // この画面を閉じる
        finish();
    }

    public void onClickKikakuInfo(View view){
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonKikakuInfo1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_COSYPLAY));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo2:
            case R.id.buttonKikakuInfo7:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_KIKAKU_OTHER3));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_GRANDPRIX));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo4:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_HAPPI_LINE));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo5:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_KIKAKU_OTHER1));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo6:
            case R.id.buttonKikakuInfo8:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_KIKAKU_OTHER4));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo9:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_KIKAKU_GUEST1));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo10:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_KIKAKU_GUEST2));
                startActivity(intent);
                break;
            case R.id.buttonKikakuInfo12:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_COSYPLAY2));
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                // バックキー押下時の処理
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
