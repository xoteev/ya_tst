package com.xoteev.ya_tst;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    // объявим массив для хранения данных элементов списка, и наш адаптер
    public LineElement[] lineElements;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);

        final ListView list = (ListView) findViewById(R.id.listView);

        // здесь парсим входящие данные
        Intent intent = getIntent();
        examineJSONFile(intent.getStringExtra("JsonStr"));

        // создаем адаптер
        Adapter mAdapter = new Adapter(MainActivity.this, lineElements);
        list.setAdapter(mAdapter);

        // устанавливаем адаптер для заполнения listView
        assert list != null;
        list.setAdapter(mAdapter);

        // установим обработчик
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int index, long id) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                // передадим данные следующей активити
                intent.putExtra("Title", lineElements[index].Title);
                intent.putExtra("ImageBig", lineElements[index].ImageUrlBig);
                intent.putExtra("Style_about", lineElements[index].Style);
                intent.putExtra("CountAlbums", lineElements[index].CountAlbums);
                intent.putExtra("CountSongs", lineElements[index].CountSongs);
                intent.putExtra("About", lineElements[index].Description);
                // адрес пока не используем, но все же запомним
                intent.putExtra("Link", lineElements[index].Link);

                // запускаем активити
                startActivity(intent);
                // добавим анимацию при смене активити
                overridePendingTransition(R.anim.anim_move_right, R.anim.anim_change_alpha);
            }
        });
    }

    // парсим полученную строку, и заполняем структура для адаптера
    void examineJSONFile(String Jsonstr)
    {
        try
        {
            JSONArray entries = new JSONArray(Jsonstr);

            String temp;

            lineElements = new LineElement[entries.length()];

            for (int i=0; i<entries.length(); i++)
            {

                JSONObject post = entries.getJSONObject(i);
                LineElement lElement = new LineElement();

                // заполним информацию о творчестве
                lElement.Title = post.optString("name");
                lElement.Link = post.optString("link");
                lElement.Description = post.optString("description");

                // обработаем массив жанров
                JSONArray jsonArray = post.getJSONArray("genres");
                temp = "";
                for (int j = 0; j < jsonArray.length(); j++) {
                    temp += jsonArray.getString(j);

                    if (jsonArray.length() != j +1)
                        temp += ", ";
                }
                lElement.Style = temp;

                // запишем информацию о кол-ве записей
                lElement.CountAlbums = post.getInt("albums");
                lElement.CountSongs = post.getInt("tracks");

                // получаем обьект с адресами картинок
                JSONObject post2 = post.getJSONObject("cover");

                lElement.ImageUrlSmall = post2.optString("small");
                lElement.ImageUrlBig = post2.optString("big");
                // запишем данные из временного обьекта в глобальный
                lineElements[i] = lElement;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}