package com.divine.yang.util.network;

import android.os.AsyncTask;
import android.util.Log;

import com.divine.yang.util.DivineCallback;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public class NetworkUtil {
    public static class UrlJudgeTask extends AsyncTask<String, Integer, Boolean> {
        private String url;
        private DivineCallback mCallback;

        public UrlJudgeTask(String url, DivineCallback mCallback) {
            this.url = url;
            this.mCallback = mCallback;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                URL address = new URL(url);
                String protocol = address.getProtocol();
                String host = address.getHost();
                int port = address.getPort();
                URL httpUrl = new URL(protocol + "://" + host + ":" + port);
                HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.disconnect();
                    return true;
                } else {
                    connection.disconnect();
                    return false;
                }
            } catch (Exception e) {
                Log.e("SchemeHelper", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                mCallback.callback(true);
            } else {
                mCallback.callback(false);

            }
        }
    }
}
