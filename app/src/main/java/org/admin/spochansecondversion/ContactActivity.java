package org.admin.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.admin.spochansecondversion.Competition.CompetitionsActivity;
import org.admin.spochansecondversion.Competition.FullCompetitionItem;
import org.admin.spochansecondversion.News.NewsActivity;

public class ContactActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextView emailText, emailAdress, phoneText, phoneNumber;

    String[] addresses = {"26bas@mail.ru"};
    String subject_developer = "Hello developer"; //тема письма
    String emailtext = "Добрый день, ...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        emailText = findViewById(R.id.emailText);
        emailAdress = findViewById(R.id.emailAdress);
        phoneText = findViewById(R.id.phoneText);
        phoneNumber = findViewById(R.id.phoneNumber);

        //устанавливаем специальный шрифт, который находится при выборе сверху слева Project, далее app/src/main/assets/fonts
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");
        Typeface roboto_bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        emailText.setTypeface(roboto_bold);
        emailAdress.setTypeface(roboto);
        phoneText.setTypeface(roboto_bold);
        phoneNumber.setTypeface(roboto);




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                startActivity(new Intent(ContactActivity.this, NewsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void emailPressed(View view) {//метод при нажатии на нашу почту
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses); //вводим сверху переменные addresses и subject
        intent.putExtra(Intent.EXTRA_SUBJECT, subject_developer);
        intent.putExtra(Intent.EXTRA_TEXT, emailtext);//текст сообщения
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}