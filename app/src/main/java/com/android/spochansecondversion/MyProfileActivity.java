package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    //private FirebaseDatabase database;
    //private DatabaseReference usersDataBaseReference, mDatabase;//эти  строки нужны для того, чтобы считывать информацию из базы данных
    //private ChildEventListener usersChildEventListener;

    private TextView userFirstNameTextView, userSecondNameTextView, userBornDateTextView, userSexTextView;
    private String bornDate;
    private ImageView avatarImageView;

    private ProgressBar progressBar;

    private static final int RC_IMAGE_PICKER = 123;//константа, которую используем в методе loadNewImage, рандомное число, которое ни на что не влияет

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage
    private StorageReference imagesStorageReference;

    private FirebaseFirestore firebaseFirestore;//для работы с cloud firestore

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
        userBornDateTextView = findViewById(R.id.userBornDateTextView);
        userSexTextView = findViewById(R.id.userSexTextView);
        avatarImageView = findViewById(R.id.avatarImageView);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(ProgressBar.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем


        FloatingActionButton editFloatingActionButton = findViewById(R.id.editFloatingActionButton);
        editFloatingActionButton.setOnClickListener(new View.OnClickListener() {//можно прописывать в разметке метод onClick или же можно в коде устанавливать setOnClickListener, исход будет одинаковый
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditMyProfileActivity.class);//первое это откуда переход, второе это куда
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        storage = FirebaseStorage.getInstance();//верхнее для работы с сообщениями, это для изображений
        imagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("Users_avatars");//в скобках это название папки в Firebase в Storage, которую мы создали вручную на сайте ранее,в которую будут помещаться изображения

        firebaseFirestore = FirebaseFirestore.getInstance();
        String currentUserUid = currentUser.getUid();

        DocumentReference userItemDocumentReference = firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid);

        userItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                userFirstNameTextView.setText(user.getFirstName());
                userSecondNameTextView.setText(user.getSecondName());
                userSexTextView.setText(user.getSex());

                Glide.with(avatarImageView.getContext())//таким образом мы загружаем изображения в наш image View
                        .load(user.getAvatarUrl())
                        .into(avatarImageView);

                if (!user.getDaysBornDate().equals("") & !user.getMonthBornDate().equals("") & !user.getYearBornDate().equals("")) {
                    bornDate = user.getDaysBornDate() + "." + user.getMonthBornDate() + "." + user.getYearBornDate();
                    userBornDateTextView.setText(bornDate);
                }
            }
        });


        //так мы работали с Realtime database, но позже перещли на cloud firestore, чтоб удобнее было рейтинг делать
        //mDatabase = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.app_country));
        //database = FirebaseDatabase.getInstance();//получаем доступ к корневой папке нашей базы данных
        //usersDataBaseReference = database.getReference().child(getResources().getString(R.string.app_country)).child("Users");//инициализируем, то есть говорим, что usersDataBaseReference это переменная связанная с папкой Users


        /*usersChildEventListener = new ChildEventListener() {//это штука реагирует на изменение в базу данных, то есть считывает имя нашего пользователя из базы данных
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                progressBar.setVisibility(ProgressBar.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем

                 if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){//если у какого-то поля в фаербас совпадет id с тем значением, которое у текущего авторизанного пользователя, то значит это именно тот пользователь и из раздела с его id берем его имя
                     userFirstNameTextView.setText(user.getFirstName());
                     userSecondNameTextView.setText(user.getSecondName());
                     userSexTextView.setText(user.getSex());


                         Glide.with(avatarImageView.getContext())//таким образом мы загружаем изображения в наш image View
                                 .load(user.getAvatarUrl())
                                 .into(avatarImageView);


                       if (!user.getDaysBornDate().equals("") & !user.getMonthBornDate().equals("") & !user.getYearBornDate().equals("")) {
                            bornDate = user.getDaysBornDate() + "." + user.getMonthBornDate() + "." + user.getYearBornDate();
                            userBornDateTextView.setText(bornDate);
                       }
                    }
                progressBar.setVisibility(ProgressBar.INVISIBLE);

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
        usersDataBaseReference.addChildEventListener(usersChildEventListener);//указываем, что лисенер будет считывать данные именно из папки users, которая прикреплена к переменной usersDataBaseReference*/

        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }


    public void loadNewImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//означает что мы создаем интент для получения контента
        intent.setType("image/*");//вместо * мог быть любой тип файлов jpeg, png и тд, но чтоб не ограничиваться только одним, то поставили звезду
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//указываем что нам нужны только локальные изображения, то есть те, которые существуют на телефоне
        //начинаем активити с целью получения результата(изображения) + активити сама закрывается после выбора изображения
        startActivityForResult(Intent.createChooser(intent, "Choose an image"), RC_IMAGE_PICKER);//первое в скобках это для того, что активити, с помощью которой можем выбирать фотку, второе это переменная(задали ее в самом верху), который будет показывать нам, куда сохраняется выбранное изображение
    }

    @Override//этот метод активируется после startActivityForResult в public void loadNewImage и нужен для добавления изображения в storage
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//этот метод нужен для работы с изображениями, создали его после написания метода public void loadNewImage
        super.onActivityResult(requestCode, resultCode, data);

        //НЕ ЗАБУДЬ ПРОПИСАТЬ В МАНИФЕСТЕ <uses-permission android:name="android.permission.INTERNET"/>

        progressBar.setVisibility(ProgressBar.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем

        String currentUserUid = currentUser.getUid();

        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            //final StorageReference imageReference  = imagesStorageReference.child(selectedImageUri.getLastPathSegment());//вот такая у нас будет ссылка на наши изображения
            //допустим uri нашего изображения //content://images/some_folder/3 и с помощью строки выше мы считываем как раз последнюю тройку благодаря getLastPathSegment() в строке выше
            final StorageReference imageReference = imagesStorageReference.child(currentUserUid);//используем такой вариант, так как здесь название изображения в Storage будет как id пользователя, соответственно после обновления изображения оно будет перезаписываться на тот же id, а старая фотка будет удаляться как бы, соответственно теперь мы не будем хранить лишние фотки в Firebase Storage

            Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();

            UploadTask uploadTask = imageReference.putFile(selectedImageUri);

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

                        String currentUserUid = currentUser.getUid();

                        try {
                            //firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid).set(user);

                            firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid)
                                    .update("avatarUrl", downloadUri.toString());
                            //mDatabase.child("Users").child(currentUserUid).child("avatarUrl").setValue(downloadUri.toString());
                            startActivity(new Intent(MyProfileActivity.this, MyProfileActivity.class));//нужно, чтоб страница обновилась и благодаря методу onCreate у нас обновилось изображение
                            //progressBar.setVisibility(ProgressBar.INVISIBLE);   нам не нужно так как кружочек загрузки нужен вплоть до загрузки заново страницы, а потом в onCreate кружочек опять включается, так что нет никакого смысла его выключать, а потом сразу же включать
                            Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.load_successful), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.update_for_60_seconds), Toast.LENGTH_LONG).show();
                        } catch (NumberFormatException nef) {
                            Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.load_fail), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyProfileActivity.this, LogInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}