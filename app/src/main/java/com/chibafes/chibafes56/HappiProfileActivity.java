package com.chibafes.chibafes56;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.chibafes.a56thchibafes.R;

/**
 * Created by llrk on 2017/03/26.
 */

public class HappiProfileActivity extends Activity {
    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happiprofile);
    }

    public void onClickButtonBack(View view) {
        // この画面を閉じる
        finish();
    }
}
