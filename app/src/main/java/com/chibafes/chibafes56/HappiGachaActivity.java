package com.chibafes.chibafes56;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;

import androidx.core.content.ContextCompat;

/**
 * Created by llrk on 2017/02/05.
 */

public class HappiGachaActivity extends Activity {
    private static final int HAPPI_GACHA_POINT = 5;
    int             nStep;
    int             nGetItem;
    AnimationSet    animationSet;
    String          sTwitterUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gacha);

        TextView textMessage = findViewById(R.id.textMessage);
        textMessage.setText(getResources().getString(R.string.GachaMessage1));

        Button buttonGacha = findViewById(R.id.buttonGacha);
        TextView textHappiPoint = findViewById(R.id.textHappiPoint);
        textHappiPoint.setText("" + Commons.readInt(this, "happi_point_57th"));
        if(Commons.readInt(this, "happi_point_57th") >= HAPPI_GACHA_POINT) {
            buttonGacha.setText("ガチャをする(" + HAPPI_GACHA_POINT + "pt)");
            buttonGacha.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButton));
            buttonGacha.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            buttonGacha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGacha(v);
                }
            });
        }
        else {
            buttonGacha.setText(getResources().getString(R.string.GachaMessage4));
            buttonGacha.setBackgroundColor(Color.DKGRAY);
            buttonGacha.setTextColor(Color.WHITE);
        }
    }

    private void doGachaAnimation() {
        ImageView imageHappi = findViewById(R.id.imageHappi);
        ImageView imageBox = findViewById(R.id.imageBox);
        PointF posImageBox = Commons.getImageSize(imageBox);

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(nStep == 10) {
                    endGachaAnimation();
                }
                else {
                    nStep++;
                    doGachaAnimation();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };

        animationSet = new AnimationSet(true);
        switch (nStep) {
            case 0: {
                RotateAnimation rotateAnimation = new RotateAnimation(0, 35, posImageBox.x / 2, posImageBox.y / 2);
                animationSet.addAnimation(rotateAnimation);
                animationSet.setDuration(300);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setAnimationListener(animationListener);
                imageBox.startAnimation(animationSet);
            }
                break;
            case 1:
            case 3: {
                RotateAnimation rotateAnimation = new RotateAnimation(35, -35, posImageBox.x / 2, posImageBox.y / 2);
                animationSet.addAnimation(rotateAnimation);
                animationSet.setDuration(500);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setAnimationListener(animationListener);
                imageBox.startAnimation(animationSet);
            }
                break;
            case 2:
            case 4: {
                RotateAnimation rotateAnimation = new RotateAnimation(-35, 35, posImageBox.x / 2, posImageBox.y / 2);
                animationSet.addAnimation(rotateAnimation);
                animationSet.setDuration(500);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setAnimationListener(animationListener);
                imageBox.startAnimation(animationSet);
            }
                break;
            case 5: {
                RotateAnimation rotateAnimation = new RotateAnimation(35, 0, posImageBox.x / 2, posImageBox.y / 2);
                animationSet.addAnimation(rotateAnimation);
                animationSet.setDuration(500);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setAnimationListener(animationListener);
                imageBox.startAnimation(animationSet);
            }
                break;
            case 6: {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.999f);
                alphaAnimation.setInterpolator(new DecelerateInterpolator());
                alphaAnimation.setDuration(300);
                alphaAnimation.setAnimationListener(animationListener);
                imageBox.startAnimation(alphaAnimation);
                /*
                nStep++;
                new Handler() {
                    public void handleMessage(Message msg) {
                        doGachaAnimation();
                    }
                }.sendEmptyMessageDelayed(0, 300);
                */
            }
                break;
            case 7: {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(scaleAnimation);
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, posImageBox.y / 4);
                animationSet.addAnimation(translateAnimation);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setDuration(300);
                animationSet.setAnimationListener(animationListener);
                imageBox.startAnimation(animationSet);
            }
                break;
            case 8: {
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(scaleAnimation);
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, posImageBox.y / 4, 0);
                animationSet.addAnimation(translateAnimation);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setDuration(300);
                animationSet.setAnimationListener(animationListener);
                imageBox.startAnimation(animationSet);
            }
                break;
            case 9: {
                imageHappi.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 0.4f, 0.2f, 0.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(scaleAnimation);
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -(Commons.getDisplaySize(this).y * 2 / 4));
                animationSet.addAnimation(translateAnimation);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setDuration(1000);
                animationSet.setAnimationListener(animationListener);
                imageHappi.startAnimation(animationSet);

                translateAnimation = new TranslateAnimation(0, 0, 0, posImageBox.y * 2);
                translateAnimation.setInterpolator(new DecelerateInterpolator());
                translateAnimation.setDuration(1000);
                imageBox.startAnimation(translateAnimation);
                break;
            }
            case 10: {
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.4f, 1.0f, 0.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(scaleAnimation);
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -(Commons.getDisplaySize(this).y * 2 / 4), 0);
                animationSet.addAnimation(translateAnimation);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setDuration(1800);
                animationSet.setAnimationListener(animationListener);
                imageHappi.startAnimation(animationSet);

                imageBox.setVisibility(View.INVISIBLE);
            }
                break;
        }
    }

    private void endGachaAnimation() {
        ImageView imageHappi = findViewById(R.id.imageHappi);
        ImageView imageBox = findViewById(R.id.imageBox);
        ImageButton buttonSkip = findViewById(R.id.buttonSkip);
        ImageButton buttonTwitter = findViewById(R.id.buttonTwitter);
        LinearLayout viewMessage = findViewById(R.id.viewMessage);

        nStep = 10;
        animationSet.cancel();

        imageHappi.setVisibility(View.VISIBLE);
        imageBox.setVisibility(View.INVISIBLE);
        buttonSkip.setVisibility(View.INVISIBLE);
        buttonTwitter.setVisibility(View.VISIBLE);
        viewMessage.setVisibility(View.VISIBLE);
    }

    public void onClickGacha(View view) {
        if(Commons.readInt(this, "happi_point_57th") >= HAPPI_GACHA_POINT) {
            Button buttonGacha = findViewById(R.id.buttonGacha);
            ImageButton buttonSkip = findViewById(R.id.buttonSkip);
            buttonGacha.setVisibility(View.INVISIBLE);
            buttonSkip.setVisibility(View.VISIBLE);
            FrameLayout viewHappiPoint = findViewById(R.id.viewHappiPoint);
            viewHappiPoint.setVisibility(View.INVISIBLE);
            LinearLayout viewMessage = findViewById(R.id.viewMessage);
            viewMessage.setVisibility(View.INVISIBLE);

            // このタイミングで抽選とフラグ立てを行う
            PList listGacha = Commons.getParsedPlist(this, "HappiCollectionList.plist");
            Array array = (Array) listGacha.getRootElement();
            nGetItem = -1;
            while(nGetItem == -1) {
                int nRate = 0;
                for(int i = 0; i < array.size(); ++i) {
                    nRate += getRate(i, array);
                }
                nRate = Commons.getRandom(nRate);
                for(int i = 0; i < array.size(); ++i) {
                    nRate -= getRate(i, array);
                    if(nRate < 0) {
                        nGetItem = i;
                        break;
                    }
                }
            }

            // プレ箱イメージ変更
            Dict dic = (Dict) array.get(nGetItem);
            int nBuf = dic.getConfigurationInteger("rate").getValue();
            ImageView imageBox = (ImageView) findViewById(R.id.imageBox);
            if(nBuf < 10) {
                imageBox.setImageResource(R.drawable.image_presentbox3);
            }
            else if(nBuf <= 20) {
                imageBox.setImageResource(R.drawable.image_presentbox2);
            }
            else {
                imageBox.setImageResource(R.drawable.image_presentbox);
            }
            imageBox.setVisibility(View.VISIBLE);

            int nItemCount = Commons.readInt(this, "getItem_57th" + nGetItem);
            if(nItemCount < 0) {
                nItemCount = 0;
            }
            Commons.writeInt(this, "getItem_57th" + nGetItem, nItemCount + 1);
            String sStatus = "はっぴガチャで「" + ((Dict)array.get(nGetItem)).getConfiguration("name").getValue() + "」が当たったよ！";
            String sUrl = "http://chibafes.com/appli.html";
            sTwitterUrl = "http://twitter.com/share?url=" + sUrl + "&text=" + sStatus + "&hashtags=千葉大祭";

            ImageView imageHappi = (ImageView) findViewById(R.id.imageHappi);
            String sGetItem = ((Dict) array.get(nGetItem)).getConfiguration("image").getValue();
            System.out.println(sGetItem);
            int nImageId = getResources().getIdentifier(sGetItem, "drawable", getPackageName());
            imageHappi.setImageResource(nImageId);
            TextView textMessage = (TextView) findViewById(R.id.textMessage);
            textMessage.setText("「" + ((Dict)array.get(nGetItem)).getConfiguration("name").getValue() + "」" + getResources().getString(R.string.GachaMessage2));

            Commons.writeInt(this, "happi_point_57th", Commons.readInt(this, "happi_point_57th") - HAPPI_GACHA_POINT);

            // ガチャ演出を開始する
            nStep = 0;
            doGachaAnimation();
        }
    }


    private int getRate(int i, Array array) {
        int nBuf = ((Dict)array.get(i)).getConfigurationInteger("rate").getValue();
        int nGotCount = Commons.readInt(this, "getItem_57th" + i);
        if(nGotCount > 0) {
            if(nGotCount > 5) {
                nGotCount = 5;
            }
            nBuf /= (nGotCount + 1);
            if(nBuf <= 0) {
                nBuf = 1;
            }
        }

        return nBuf;
    }

    public void onClickSkip(View view) {
        endGachaAnimation();
    }

    public void onClickTwitter(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sTwitterUrl));
        startActivity(intent);
    }

    public void onClickButtonBack(View view) {
        // この画面を閉じる
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// バックキー押下時の処理
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
