package com.xoteev.ya_tst;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends AppCompatActivity {

    TextView tView;         // инфо о состоянии
    ProgressBar pBar;       // прогресбар в момент соединения
    FrameLayout frLayout;
    ImageView imageView;    // отображаем при ошибке соединения
    private Animation mFadeInAnimation, mFadeOutAnimation;
    int animCount = 0;      // считаем кол-во отображений анимации

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        DownloadData();
    }
// отображаем прогресбар и пробуем получить данные
    void DownloadData(){
        pBar =  (ProgressBar) findViewById(R.id.pBar);
        pBar.setVisibility(View.VISIBLE);

        // установим прогресбар
        frLayout = (FrameLayout) findViewById(R.id.frLayout);
        frLayout.removeAllViews();
        frLayout.addView(pBar);

        tView = (TextView) findViewById(R.id.tView);
        tView.setText("Подождите,\nвыполняется загрузка");

        GetJson getJson = new GetJson();
        getJson.execute("http://download.cdn.yandex.net/mobilization-2016/artists.json");
    }

    // анимация появления картинки когда нет связи
    Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            // показываем анимацию определенное кол-во раз
            if(animCount < 3)
                imageView.startAnimation(mFadeOutAnimation);
            else{
                tView.setText("Повторить?");
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    };
    // анимация затухания
    Animation.AnimationListener animationFadeOutListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            ++animCount;
            imageView.startAnimation(mFadeInAnimation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    };
    // получаем данные
    class GetJson extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tView = (TextView) findViewById(R.id.tView);
            tView.setText("Подождите,\nвыполняется загрузка");
        }

        /*@Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }*/

        @Override
        protected String doInBackground(String ... urls) {

            for (String strUrl : urls) {
                try {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;

                    URL url = new URL(strUrl);

                    // покажем пользователю прогресбар
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    /*if (isCancelled())
                        return null;*/

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            if(strJson.isEmpty()){
                // json не пришел
                tView = (TextView) findViewById(R.id.tView);
                tView.setText("Неудалось выполнить\nзагрузку");

                // найдем лэйаут
                frLayout = (FrameLayout) findViewById(R.id.frLayout);

                // скроем прогресбар
                pBar =  (ProgressBar) findViewById(R.id.pBar);
                pBar.setVisibility(View.INVISIBLE);

                // информируем об отсутствии связи
                imageView = new ImageView(StartActivity.this);
                imageView.setImageResource(R.drawable.ic_houston_problem);
                imageView.setClickable(true);
                imageView.setAlpha((float) 0.3);
                frLayout.addView(imageView);

                // добавляем анимацию
                mFadeInAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.icon_fadein);
                mFadeOutAnimation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.icon_fadeout);
                mFadeInAnimation.setAnimationListener(animationFadeInListener);
                mFadeOutAnimation.setAnimationListener(animationFadeOutListener);

                // при запуске начинаем с анимации исчезновения
                imageView.startAnimation(mFadeOutAnimation);

                // повторим загрузку
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animCount = 0;
                        imageView.clearAnimation();
                        DownloadData();
                    }
                });
            }
            else{
                // json получили
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                //передадим данные следующей активити
                intent.putExtra("JsonStr", strJson);

                // запускаем активити
                startActivity(intent);
                // добавим анимацию при смене активити
                overridePendingTransition(R.anim.anim_move_right, R.anim.anim_change_alpha);
                StartActivity.this.finish();
            }
        }
    }
}