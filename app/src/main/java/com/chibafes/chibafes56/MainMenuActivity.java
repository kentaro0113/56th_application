package com.chibafes.chibafes56;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * MainMenu Activity
 * Created by llrk on 2017/08/04.
 * メインメニュー画面
 */
public class MainMenuActivity extends FragmentActivity implements HttpPostAsync.AsyncTaskCallback {
    private FragmentManager fragmentManager;
    private TextView textTitle;
    private String[] arrTabImages = {"icon_info", "icon_search", "icon_timetable", "icon_map", "icon_happi"};
    private int[] arrTabItemIds = {R.id.buttonMenuInfo, R.id.buttonMenuSearch, R.id.buttonMenuTimeTable, R.id.buttonMenuMap, R.id.buttonMenuHappi};

    // アンケート用変数
    private AlertDialog alartEnquete = null;
    private int nQuestionNo;
    private int nQuestionType;
    private Spinner spinnerSelect;
    private EditText editComment;
    private boolean bAppeared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーの表示設定：非表示にする
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // このActivityに関連づけるレイアウトの設定
        setContentView(R.layout.activity_mainmenu);

        fragmentManager = getSupportFragmentManager();
        textTitle = (TextView)findViewById(R.id.textTitle);

        // 初期メニューはINFOを表示する
        onClickButtonInfo(null);

        // 画面遷移時に状態によってダイアログを表示する
        bAppeared = false;
        checkPopup();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 画面が表示されるたびに呼び出される
    }

    public void onClickButtonInfo(View view) {
        Fragment fragment = new InfoActivity();
        changeFragment(0, fragment, R.string.TitleInfo);
    }
    public void onClickButtonSearch(View view) {
        Fragment fragment = new KikakuSearchActivity();
        changeFragment(1, fragment, R.string.TitleSearch);
    }
    public void onClickButtonTimeTable(View view) {
        Fragment fragment = new TimeTableActivity();
        changeFragment(2, fragment, R.string.TitleTimeTable);
    }
    public void onClickButtonMap(View view) {
        Fragment fragment = new MapActivity();
        changeFragment(3, fragment, R.string.TitleMap);
    }
    public void onClickButtonHappi(View view) {
        Fragment fragment = new HappiActivity();
        changeFragment(4, fragment, R.string.TitleHappi);
    }

    // 画面切り替え処理
    private void changeFragment(int index, Fragment fragment, int nTitleId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.contents,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        textTitle.setText(getResources().getString(nTitleId));
        // タブの表示変更
        for(int i = 0; i < 5; ++i) {
            String sName = arrTabImages[i];
            ImageButton button = (ImageButton) findViewById(arrTabItemIds[i]);
            if(i == index) {
                sName = sName + "_on";
            }
            button.setImageResource(getResources().getIdentifier(sName, "drawable", getPackageName()));
        }
    }

    public void onClickButtonSetting(View view) {
        Intent intent = new Intent(MainMenuActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void checkPopup() {
        // 当日初回起動かどうかのチェック
        String sLastRunDay = Commons.readString(this, "LastRunDay");
        String sToday = Commons.getTimeString("yyyyMMdd");
        if(!sToday.equals(sLastRunDay)) {
            // 初回起動ならはっぴポイントを加算する
            int nGetPoint = 5;
            Commons.writeInt(this, "happi_point", Commons.readInt(this, "happi_point") + nGetPoint);
            Commons.writeString(this, "LastRunDay", sToday);

            new AlertDialog.Builder(this)
                    .setMessage(String.format(getResources().getString(R.string.GetLoginBonus), nGetPoint))
                    .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkPopup();
                        }
                    }).show();
            return;
        }

        // 特別ログインボーナス（11/2~5のログイン時に追加で30pt）
        if(sToday.equals("20171102") || sToday.equals("20171103") || sToday.equals("20171104") || sToday.equals("20171105")) {
            if(Commons.readInt(this, "login" + sToday) == 0) {

                int nGetPoint = 30;
                Commons.writeInt(this, "happi_point", Commons.readInt(this, "happi_point") + nGetPoint);
                Commons.writeInt(this, "login" + sToday, 1);

                new AlertDialog.Builder(this)
                        .setMessage(String.format(getResources().getString(R.string.GetLoginBonus2), nGetPoint))
                        .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPopup();
                            }
                        }).show();
                return;
            }
        }


        if(!bAppeared) {
            // アンケートのチェック
            String sEnquete = Commons.readString(this, "data_enquete");
            if(sEnquete != null) {
                try {
                    JSONArray jsonArray = new JSONArray(sEnquete);
                    JSONObject json = jsonArray.getJSONObject(0);
                    nQuestionNo = json.getInt("id");
                    int nAnswerNo = Commons.readInt(this, "answer_no");
                    if ((nAnswerNo == 0 || nAnswerNo != nQuestionNo) && nQuestionNo != 0) {
                        ScrollView viewEnquete = (ScrollView) getLayoutInflater().inflate(R.layout.enquete_window_view, null);
                        TextView textQuestion = (TextView) viewEnquete.findViewById(R.id.textQuestion);
                        textQuestion.setText(json.getString("question"));
                        spinnerSelect = (Spinner) viewEnquete.findViewById(R.id.spinnerSelect);
                        editComment = (EditText) viewEnquete.findViewById(R.id.editComment);

                        nQuestionType = Statics.TYPE_SELECT;
                        String sAnswer = json.getString("answer");
                        if(sAnswer == null || sAnswer.length() <= 0) {
                            nQuestionType = Statics.TYPE_TEXT;
                        }
                        switch (nQuestionType) {
                            case Statics.TYPE_SELECT:
                                String[] arrAnswers = sAnswer.split(",");
                                spinnerSelect.setVisibility(View.VISIBLE);
                                editComment.setVisibility(View.GONE);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                        android.R.layout.simple_spinner_item, arrAnswers);
                                spinnerSelect.setAdapter(adapter);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSelect.setSelection(0);
                                break;

                            case Statics.TYPE_TEXT:
                                spinnerSelect.setVisibility(View.GONE);
                                editComment.setVisibility(View.VISIBLE);
                                editComment.setText("");
                                break;
                        }

                        alartEnquete = new AlertDialog.Builder(MainMenuActivity.this)
                                .setView(viewEnquete)
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bAppeared = true;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                // バックキー押下時の処理
                case KeyEvent.KEYCODE_BACK:
                    // アプリを終了する
                    // 終了の確認ダイアログ生成
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
                    // 終了確認のメッセージを設定する
                    alertDlg.setMessage(getResources().getString(R.string.ConfirmFinish));
                    // OKボタンを設定する
                    alertDlg.setPositiveButton(
                            getResources().getString(R.string.ButtonOk),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    // キャンセルボタンを設定知る
                    alertDlg.setNegativeButton(
                            getResources().getString(R.string.ButtonCancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    // ダイアログを表示する
                    alertDlg.create().show();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /*
        アンケート関連処理
     */
    // アンケート送信ボタンが押された時
    public void onClickSend(View view) {
        // 送信用パラメータとして、アンケート情報を文字列として保持
        String paramString = "question_id=" + nQuestionNo + "&user_id=" + Commons.readLong(this, "UserID");
        // アンケートの種類による分岐
        switch(nQuestionType) {
            // 選択式
            case Statics.TYPE_SELECT: {
                // 送信用パラメータに選択肢の番号を追加
                paramString = paramString + "&answer_select=" + spinnerSelect.getSelectedItemPosition() + "&answer_word";
                break;
            }
            // 記述式
            case Statics.TYPE_TEXT: {
                // 送信用パラメータに回答文の情報を追加
                paramString = paramString + "&answer_select=-1&answer_word=" + editComment.getText();
                break;
            }
        }
        // 通信用オブジェクトを生成
        HttpPostAsync postObject = new HttpPostAsync(this);
        // 通信処理を実行
        postObject.execute(Statics.URL_ENQUETE, paramString);
        // ダイアログを閉じる
        alartEnquete.dismiss();
    }
    // アンケートキャンセルボタンが押された時
    public void onClickCancel(View view) {
        alartEnquete.dismiss();
    }

    public void preExecute() {
    }
    // アンケートの送信が完了した時の処理
    public void postExecute(String result, boolean bError) {
        if(!bError) {
            // 通信に成功した場合
            if(result.contains("OK")) {
                int nGetPoint = 10;
                Commons.writeInt(this, "happi_point", Commons.readInt(this, "happi_point") + nGetPoint);
                // 回答済みのアンケート番号を今回のアンケート番号に更新する
                Commons.writeInt(this, "answer_no", nQuestionNo);
                // 正常に完了した旨のダイアログを表示
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.EnqueteFinishTitle))
                        .setMessage(String.format(getResources().getString(R.string.EnqueteFinish), nGetPoint))
                        .setPositiveButton(getResources().getString(R.string.ButtonOk), null)
                        .show();
            }
        }
        else {
            // エラーが発生した旨のダイアログを表示
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.EnqueteErrorTItle))
                    .setMessage(getResources().getString(R.string.EnqueteError))
                    .setPositiveButton(getResources().getString(R.string.ButtonOk), null)
                    .show();
        }
    }

    public void progressUpdate(int progress) {
    }
    public void cancel() {
        // エラーが発生した旨のダイアログを表示
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.EnqueteErrorTItle))
                .setMessage(getResources().getString(R.string.EnqueteError))
                .setPositiveButton(getResources().getString(R.string.ButtonOk), null)
                .show();
    }




}

