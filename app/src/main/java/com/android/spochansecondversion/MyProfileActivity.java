package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference usersDataBaseReference;//эти две строки нужны для того, чтобы считывать информацию(имя) из базы данных и чтобы под сообщением писалось кто отправил то вообще
    private ChildEventListener usersChildEventListener;

    private TextView userFirstNameTextView, userSecondNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_myProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_news:
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_myProfile:
                        return true;
                    case R.id.navigation_competitions:
                        startActivity(new Intent(getApplicationContext(), CompetitionsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        userFirstNameTextView = findViewById(R.id.userFirstNameTextView);
        userSecondNameTextView = findViewById(R.id.userSecondNameTextView);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();//получаем доступ к корневой папке нашей базы данных
        usersDataBaseReference = database.getReference().child("Users");//инициализируем, то есть говорим, что usersDataBaseReference это переменная связанная с папкой Users

        usersChildEventListener = new ChildEventListener() {//это штука реагирует на изменение в базу данных, то есть считывает имя нашего пользователя из базы данных
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){//если у какого-то поля в фаербас совпадет id с тем значением, которое у текущего авторизанного пользователя, то значит это именно тот пользователь и из раздела с его id берем его имя
                    userFirstNameTextView.setText(user.getFirstName());
                    userSecondNameTextView.setText(user.getSecondName());
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
}