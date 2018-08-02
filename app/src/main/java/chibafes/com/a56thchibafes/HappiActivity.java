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
