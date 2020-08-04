package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddCompetitionsActivity extends AppCompatActivity {

    private DatabaseReference competitionsDataBaseReference;

    private EditText competitionTitleEditText, competitionLocationEditText, competitionAddressEditText, competitionDescriptionEditText;
    private EditText daysBornDateEditText, monthBornDateEditText, yearBornDateEditText;
    private String childReferenceName;

    private static final int RC_IMAGE_PICKER = 321;//константа, которую используем в методе loadNewImage, рандомное число, которое ни на что не влияет

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage не рядом с изображениями
    private StorageReference competitionImagesStorageReference;


    private String currentCompetitionImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_competitions);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_competitions);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                    case R.id.navigation_news:
                    case R.id.navigation_myProfile:
                    case R.id.navigation_competitions:
                        Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.edit_notification), Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        competitionsDataBaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.app_country)).child("Competitions");

        storage = FirebaseStorage.getInstance();
        competitionImagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("Competitions_images");//в скобках это название папки в Firebase в Storage, которую мы создали вручную на сайте ранее,в которую будут помещаться изображения


        competitionTitleEditText = findViewById(R.id.competitionTitleEditText);
        competitionLocationEditText = findViewById(R.id.competitionLocationEditText);
        competitionAddressEditText = findViewById(R.id.competitionAddressEditText);
        competitionDescriptionEditText = findViewById(R.id.competitionDescriptionEditText);
        daysBornDateEditText = findViewById(R.id.daysBornDateEditText);
        monthBornDateEditText = findViewById(R.id.monthBornDateEditText);
        yearBornDateEditText = findViewById(R.id.yearBornDateEditText);


    }

    public void cancelButton(View view) {//онклик метод для кнопки Отмены
        startActivity(new Intent(AddCompetitionsActivity.this, CompetitionsActivity.class));
    }

    public void addButton(View view) {
        /*Competition competition = new Competition();
        competition.setCompetitionTitle(competitionTitleEditText.getText().toString().trim());
        competition.setCompetitionData(competitionDataEditText.getText().toString().trim());
        competition.setCompetitionLocation(competitionLocationEditText.getText().toString().trim());
        competition.setCompetitionAddress(competitionAddressEditText.getText().toString().trim());
        competition.setCompetitionDescription(competitionDescriptionEditText.getText().toString().trim());
        competition.setCompetitionImageUrl(currentCompetitionImageUrl);
        childReferenceName = competitionTitleEditText.getText().toString().trim() + " - " + competitionDataEditText.getText().toString().trim();
        competitionsDataBaseReference.child(childReferenceName).setValue(competition);*/
        /*Map<String, Object> competition = new HashMap<>();
        competition.put("name", "Tokyo");
        competition.put("country", "Japan");*///Хороший вариант, то есть устанавливаешь сразу и поля и их значения

        try {//для того, чтобы люди не вводили буквенные выражения для даты

            if (!daysBornDateEditText.getText().toString().trim().equals("") || !monthBornDateEditText.getText().toString().trim().equals("") || !yearBornDateEditText.getText().toString().trim().equals("")) {

                if (daysBornDateEditText.getText().toString().trim().length() != 2) {//проверяем чтоб дата имела только два знака
                    Toast.makeText(this, "Дни должны содержать 2 символа", Toast.LENGTH_SHORT).show();
                }else if (monthBornDateEditText.getText().toString().trim().length() != 2) {//проверяем чтоб дата имела только два знака
                    Toast.makeText(this, "Месяц должен содержать 2 символа", Toast.LENGTH_SHORT).show();
                }else if (yearBornDateEditText.getText().toString().trim().length() != 4) {//проверяем чтоб дата имела только четыре знака
                    Toast.makeText(this, "Год должен содержать 4 символа", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(daysBornDateEditText.getText().toString().trim()) >31) {
                    Toast.makeText(this, "Формат даты неверный", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(monthBornDateEditText.getText().toString().trim()) >12) {
                    Toast.makeText(this, "Формат даты неверный", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(yearBornDateEditText.getText().toString().trim()) >2099) {
                    Toast.makeText(this, "Формат даты неверный", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(yearBornDateEditText.getText().toString().trim()) <1900) {
                    Toast.makeText(this, "Формат даты неверный", Toast.LENGTH_SHORT).show();
                }else if (competitionTitleEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Введите значение заголовка", Toast.LENGTH_SHORT).show();
                }else if (competitionLocationEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Введите значение города", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Competition competition = new Competition();
                    competition.setCompetitionTitle(competitionTitleEditText.getText().toString().trim());
                    competition.setCompetitionData(daysBornDateEditText.getText().toString().trim() + " . " + monthBornDateEditText.getText().toString().trim() + " . " + yearBornDateEditText.getText().toString().trim());
                    competition.setCompetitionLocation(competitionLocationEditText.getText().toString().trim());
                    competition.setCompetitionAddress(competitionAddressEditText.getText().toString().trim());
                    competition.setCompetitionDescription(competitionDescriptionEditText.getText().toString().trim());
                    competition.setCompetitionImageUrl(currentCompetitionImageUrl);

                    //Благодаря такой записи .document() соревнования будут располагаться по дате, при этом самые ближайшие в самом верху
                    db.collection("Competitions" + getResources().getString(R.string.app_country)).document(yearBornDateEditText.getText().toString().trim() + monthBornDateEditText.getText().toString().trim() + daysBornDateEditText.getText().toString().trim()).set(competition);
                    Toast.makeText(AddCompetitionsActivity.this, "Успешно", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddCompetitionsActivity.this, CompetitionsActivity.class));
                }
            } else {
                Toast.makeText(AddCompetitionsActivity.this, "Введите все значения", Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException nef) {
            Toast.makeText(AddCompetitionsActivity.this, "Введите численное значение", Toast.LENGTH_SHORT).show();
        }


    }

    public void loadNewImage(View view) {//онклик метод для нашей кнопки Загрузить фото
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//означает что мы создаем интент для получения контента
        intent.setType("image/*");//вместо * мог быть любой тип файлов jpeg, png и тд, но чтоб не ограничиваться только одним, то поставили звезду
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//указываем что нам нужны только локальные изображения, то есть те, которые существуют на телефоне
        //начинаем активити с целью получения результата(изображения) + активити сама закрывается после выбора изображения
        startActivityForResult(Intent.createChooser(intent, "Choose an image"), RC_IMAGE_PICKER);//первое в скобках это для того, что активити, с помощью которой можем выбирать фотку, второе это переменная(задали ее в самом верху), который будет показывать нам, куда сохраняется выбранное изображение
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//этот метод нужен для работы с изображениями
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            final StorageReference imageReference  = competitionImagesStorageReference.child(selectedImageUri.getLastPathSegment());//вот такая у нас будет ссылка на наши изображения
            //допустим uri нашего изображения //content://images/some_folder/3 и с помощью строки выше мы считываем как раз последнюю тройку благодаря getLastPathSegment() в строке выше

            Toast.makeText(AddCompetitionsActivity.this, "Ожидайте...", Toast.LENGTH_LONG).show();
            UploadTask uploadTask = imageReference.putFile(selectedImageUri);

            uploadTask = imageReference.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {//то есть все хорошо работает, нет проблем ни с изображениями, ни с какими-то другими вещами
                        Uri downloadUri = task.getResult();

                        currentCompetitionImageUrl = downloadUri.toString();
                        Toast.makeText(AddCompetitionsActivity.this, "Изображение успешно загружено", Toast.LENGTH_LONG).show();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}