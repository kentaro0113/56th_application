package chibafes.com.a56thchibafes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class HappiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happi);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gacha);

        TextView textMessage = (TextView) findViewById(R.id.textMessage);
        textMessage.setText(getResources().getString(R.string.GachaMessage1));

        Button buttonGacha = (Button) findViewById(R.id.buttonGacha);
        TextView textHappiPoint = (TextView) findViewById(R.id.textHappiPoint);
        textHappiPoint.setText("" + Commons.readInt(this, "happi gacha"));
        if(Commons.readInt(this, "happi_point") >= HAPPI_GACHA_POINT) {
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
}

imageHappi.startAnimation(animationSet);private void doGachaAnimation() {
        ImageView imageHappi = (ImageView) findViewById(R.id.imageHappi);
        ImageView imageBox = (ImageView) findViewById(R.id.imageBox);
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
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -(int)(Commons.getDisplaySize(this).y * 2 / 4));
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


        imageBox.setVisibility(View.INVISIBLE);
        }
        break;
        }
        }

private void endGachaAnimation() {
        ImageView imageHappi = (ImageView) findViewById(R.id.imageHappi);
        ImageView imageBox = (ImageView) findViewById(R.id.imageBox);
        ImageButton buttonSkip = (ImageButton) findViewById(R.id.buttonSkip);
        ImageButton buttonTwitter = (ImageButton) findViewById(R.id.buttonTwitter);
        LinearLayout viewMessage = (LinearLayout) findViewById(R.id.viewMessage);

        nStep = 10;
        animationSet.cancel();

        imageHappi.setVisibility(View.VISIBLE);
        imageBox.setVisibility(View.INVISIBLE);
        buttonSkip.setVisibility(View.INVISIBLE);
        buttonTwitter.setVisibility(View.VISIBLE);
        viewMessage.setVisibility(View.VISIBLE);
        }

public void onClickGacha(View view) {
        if(Commons.readInt(this, "happi_point") >= HAPPI_GACHA_POINT) {
        Button buttonGacha = (Button) findViewById(R.id.buttonGacha);
        ImageButton buttonSkip = (ImageButton) findViewById(R.id.buttonSkip);
        buttonGacha.setVisibility(View.INVISIBLE);
        buttonSkip.setVisibility(View.VISIBLE);
        FrameLayout viewHappiPoint = (FrameLayout) findViewById(R.id.viewHappiPoint);
        viewHappiPoint.setVisibility(View.INVISIBLE);
        LinearLayout viewMessage = (LinearLayout) findViewById(R.id.viewMessage);
        viewMessage.setVisibility(View.INVISIBLE);

        // このタイミングで抽選とフラグ立てを行う
        PList listGacha = Commons.getParsedPlist(this, "HappiCollectionList.plist");
        Array array = (Array) listGacha.getRootElement();
        nGetItem = -1;
        while(nGetItem == -1) {
        int nRate = 0;
        for(int i = 0; i < array£
    /*


                NSString* sBuf = @"https://itunes.apple.com/us/app/%E7%AC%AC55%E5%9B%9E%E5%8D%83%E8%91%89%E5%A4%A7%E7%A5%AD%E3%82%A2%E3%83%97%E3%83%AA-for-you/id1299542669?l=ja&ls=1&mt=8";
                NSString *sEscapedUrl = [sBuf stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet alphanumericCharacterSet]];
                sBuf = [NSString stringWithFormat:@"はっぴガチャで「%@」が当たったよ！", [[array objectAtIndex:nGetItem] objectForKey:@"name"]];
                //NSString *sEscapedStatus = [sBuf stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                sTwitterUrl = [NSString stringWithFormat:@"http://twitter.com/share?url=%@&text=%@&hashtags=千葉大祭", sEscapedUrl, sBuf];
                // ガチャ演出を開始する
                nStep = 0;

            }
     */


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


