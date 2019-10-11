package com.chibafes.chibafes56;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpPostAsync
 * Created by llrk on 2017/03/09.
 * 非同期通信処理を管理するクラス
 */

public class HttpPostAsync extends AsyncTask<String, Integer, String> {
    private AsyncTaskCallback callback = null;
    private boolean bError;

    HttpPostAsync(AsyncTaskCallback _callback) {
        super();
        this.callback = _callback;
        bError = false;
    }

    private HttpURLConnection con = null;

    @Override
    protected String doInBackground(String... params) {
      //  HttpURLConnection con = null;
        URL url;
        String result = null;

        try {
            url = new URL(params[0]);
            con = (HttpURLConnection) url.openConnection();

            // キャッシュを使用しない
            con.setUseCaches(false);

            // リダイレクトを許可しない
            con.setInstanceFollowRedirects(false);

            // データを読み取る
            con.setDoInput(true);

            // データを書き込む
            con.setDoOutput(true);

            // タイムアウト
            con.setReadTimeout(6000);
            con.setConnectTimeout(6000);

            // 接続
            con.connect(); //

            // データの書き込み
            if(params.length > 1) {
                OutputStream out = null;
                try {
                    out = con.getOutputStream();
                    out.write(params[1].getBytes("UTF-8"));
                    out.flush();
                } catch (IOException e) {
                    // POST送信エラー
                    e.printStackTrace();
                    bError = true;
                    return null;
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }

            // ステータスコードの取得
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 正常

                // データの読み取り
                InputStream in = null;
                try {
                    in = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String buf;
                    while ((buf = br.readLine()) != null) {
                        sb.append(buf).append("\n"); // 改行コードを付与する形に変更
                        Log.d("HTTP_POST", buf);
                    }
                    result = new String(sb);

                } catch (IOException e) {
                    e.printStackTrace();
                    bError = true;
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            bError = true;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.preExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.cancel();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            final int status = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callback.postExecute(result, bError);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callback.progressUpdate(values[0]);
    }

    public interface AsyncTaskCallback {
        void preExecute();
        void postExecute(String result, boolean bError);
        void progressUpdate(int progress);
        void cancel();
    }
}
