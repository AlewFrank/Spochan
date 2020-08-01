package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditMyProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText, secondNameEditText;

    FirebaseUser currentUser;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference usersDataBaseReference, mDatabase;//эти две строки нужны для того, чтобы считывать информацию(имя) из базы данных
    private ChildEventListener usersChildEventListener;
    private EditText daysBornDateEditText, monthBornDateEditText, yearBornDateEditText;
    private RadioButton maleRadioButton, femaleRadioButton;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_myProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                    case R.id.navigation_news:
                    case R.id.navigation_myProfile:
                    case R.id.navigation_competitions:
                        Toast.makeText(EditMyProfileActivity.this, getResources().getString(R.string.edit_notification), Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        setTitle(getResources().getString(R.string.edit_activity));//устанавливаем заголовок activity

        firstNameEditText = findViewById(R.id.firstNameEditText);
        secondNameEditText = findViewById(R.id.secondNameEditText);
        daysBornDateEditText = findViewById(R.id.daysBornDateEditText);
        monthBornDateEditText = findViewById(R.id.monthBornDateEditText);
        yearBornDateEditText = findViewById(R.id.yearBornDateEditText);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();//получаем доступ к корневой папке нашей базы данных
        usersDataBaseReference = database.getReference().child("Users");//инициализируем, то есть говорим, что usersDataBaseReference это переменная связанная с папкой Users

        usersChildEventListener = new ChildEventListener() {//это штука реагирует на изменение в базу данных, то есть считывает имя нашего пользователя из базы данных
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){//если у какого-то поля в фаербас совпадет id с тем значением, которое у текущего авторизанного пользователя, то значит это именно тот пользователь и из раздела с его id берем его имя
                    firstNameEditText.setText(user.getFirstName());
                    secondNameEditText.setText(user.getSecondName());


                    if (!user.getDaysBornDate().equals("") & !user.getMonthBornDate().equals("") & !user.getYearBornDate().equals("")) {
                        daysBornDateEditText.setText(user.getDaysBornDate());
                        monthBornDateEditText.setText(user.getMonthBornDate());
                        yearBornDateEditText.setText(user.getYearBornDate());
                    }


                    if (user.getSex().equals(getResources().getString(R.string.gender_male))){
                        maleRadioButton.setChecked(true);
                        femaleRadioButton.setChecked(false);
                        gender = getResources().getString(R.string.gender_male);//эта строка очень важна, так как если мы не меняем ничего и сохраняем, то gender получается ничему у нас не равен, а следовательно в firebase отправляется ничего = поле sex удаляется
                    }else if (user.getSex().equals(getResources().getString(R.string.gender_female))){
                        femaleRadioButton.setChecked(true);
                        maleRadioButton.setChecked(false);
                        gender = getResources().getString(R.string.gender_female);
                    }else if (user.getSex().equals(getResources().getString(R.string.gender_not_stated))){
                        femaleRadioButton.setChecked(false);
                        maleRadioButton.setChecked(false);
                        gender = getResources().getString(R.string.gender_not_stated);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        usersDataBaseReference.addChildEventListener(usersChildEventListener);//указываем, что лисенер будет считывать данные именно из папки users, которая прикреплена к переменной usersDataBaseReference
    }

    public void cancelButton(View view) {//онклик метод для кнопки "Отмена"
        startActivity(new Intent(EditMyProfileActivity.this, MyProfileActivity.class));
    }

    public void editButton(View view) {//онклик метод для кнопки "Сохранить"
        String currentUserUid = currentUser.getUid();

        if (!firstNameEditText.getText().toString().trim().equals("")) {
            mDatabase.child("Users").child(currentUserUid).child("firstName").setValue(firstNameEditText.getText().toString().trim());
        }

        if (!secondNameEditText.getText().toString().trim().equals("")) {
            mDatabase.child("Users").child(currentUserUid).child("secondName").setValue(secondNameEditText.getText().toString().trim());
        }

        mDatabase.child("Users").child(currentUserUid).child("sex").setValue(gender);//если ничего не выбрано, то мы поставили в методе public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {, что по умолчанию будет пол не указано

        try {//для того, чтобы люди не вводили буквенные выражения для даты

        if (!daysBornDateEditText.getText().toString().trim().equals("") || !monthBornDateEditText.getText().toString().trim().equals("") || !yearBornDateEditText.getText().toString().trim().equals("")) {
            //если все EditText пустые, то значит человек еще не притрагивался к их заполнению, следовательно, можно просто выйти из активити(переход к ветке else), если человек хочет изменять дату потом

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
            }else {
                mDatabase.child("Users").child(currentUserUid).child("daysBornDate").setValue(daysBornDateEditText.getText().toString().trim());
                mDatabase.child("Users").child(currentUserUid).child("monthBornDate").setValue(monthBornDateEditText.getText().toString().trim());
                mDatabase.child("Users").child(currentUserUid).child("yearBornDate").setValue(yearBornDateEditText.getText().toString().trim());

                Toast.makeText(EditMyProfileActivity.this, "Успешно", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditMyProfileActivity.this, MyProfileActivity.class));
            }
        } else {//если он не начал даже заполнять дату, то просто выходим из активити и все, ничего страшного, дату заполнит потом
            mDatabase.child("Users").child(currentUserUid).child("sex").setValue(gender);
            Toast.makeText(EditMyProfileActivity.this, "Успешно", Toast.LENGTH_LONG).show();
            startActivity(new Intent(EditMyProfileActivity.this, MyProfileActivity.class));
        }
        } catch (NumberFormatException nef) {
            Toast.makeText(EditMyProfileActivity.this, "Введите численное значение", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if (checked) {
            switch(view.getId()) {
                case R.id.maleRadioButton:
                    if (checked)
                        gender = getResources().getString(R.string.gender_male);
                    femaleRadioButton.setChecked(false);
                    break;
                case R.id.femaleRadioButton:
                    if (checked)
                        gender = getResources().getString(R.string.gender_female);
                    maleRadioButton.setChecked(false);
            }
        } else {
            gender = getResources().getString(R.string.gender_not_stated);
        }
    }
}