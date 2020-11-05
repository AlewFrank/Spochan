package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.spochansecondversion.Rating.RatingActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRatingMembers extends AppCompatActivity {

    private EditText firstPlaceFirstName, secondPlaceFirstName, thirdPlaceFirstName, forthPlaceFirstName, fifthPlaceFirstName;

    private EditText firstPlaceSecondName, secondPlaceSecondName, thirdPlaceSecondName, forthPlaceSecondName, fifthPlaceSecondName;

    private EditText firstPlacePoints, secondPlacePoints, thirdPlacePoints, forthPlacePoints, fifthPlacePoints;

    private String onGroupClick;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rating_members);


        //три строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
//        ActionBar actionBar =getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        firstPlaceFirstName = findViewById(R.id.firstPlaceFirstName);
        firstPlaceSecondName = findViewById(R.id.firstPlaceSecondName);
        firstPlacePoints = findViewById(R.id.firstPlacePoints);

        secondPlaceFirstName = findViewById(R.id.secondPlaceFirstName);
        secondPlaceSecondName = findViewById(R.id.secondPlaceSecondName);
        secondPlacePoints = findViewById(R.id.secondPlacePoints);

        thirdPlaceFirstName = findViewById(R.id.thirdPlaceFirstName);
        thirdPlaceSecondName = findViewById(R.id.thirdPlaceSecondName);
        thirdPlacePoints = findViewById(R.id.thirdPlacePoints);

        forthPlaceFirstName = findViewById(R.id.forthPlaceFirstName);
        forthPlaceSecondName = findViewById(R.id.forthPlaceSecondName);
        forthPlacePoints = findViewById(R.id.forthPlacePoints);

        fifthPlaceFirstName = findViewById(R.id.fifthPlaceFirstName);
        fifthPlaceSecondName = findViewById(R.id.fifthPlaceSecondName);
        fifthPlacePoints = findViewById(R.id.fifthPlacePoints);

        Intent forEditActivityIntent = getIntent(); //получаем интент с названием группы, которую хотим редактировать
        onGroupClick = forEditActivityIntent.getStringExtra("onGroupClick");

        workingWithFirebase("1");
        workingWithFirebase("2");
        workingWithFirebase("3");
        workingWithFirebase("4");
        workingWithFirebase("5");


        FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDataToFirebase("1");
                setDataToFirebase("2");
                setDataToFirebase("3");
                setDataToFirebase("4");
                setDataToFirebase("5");


                Toast.makeText(EditRatingMembers.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditRatingMembers.this, RatingActivity.class));
            }
        });


    }

    private void workingWithFirebase (final String groupId) {//метод считывает данные из firebase

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference groupItemDocumentReference = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document(onGroupClick).collection(onGroupClick).document(groupId);

        groupItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);


                if (groupId.equals("1")) {
                    if (user.getFirstName() != null || user.getSecondName() != null) {
                        firstPlaceFirstName.setText(user.getFirstName());
                        firstPlaceSecondName.setText(user.getSecondName());
                        firstPlacePoints.setText(user.getUserPoints());
                    }
                }

                if (groupId.equals("2")) {
                    if (user.getFirstName() != null || user.getSecondName() != null) {
                        secondPlaceFirstName.setText(user.getFirstName());
                        secondPlaceSecondName.setText(user.getSecondName());
                        secondPlacePoints.setText(user.getUserPoints());
                    }
                }

                if (groupId.equals("3")) {
                    if (user.getFirstName() != null || user.getSecondName() != null) {
                        thirdPlaceFirstName.setText(user.getFirstName());
                        thirdPlaceSecondName.setText(user.getSecondName());
                        thirdPlacePoints.setText(user.getUserPoints());
                    }
                }

                if (groupId.equals("4")) {
                    if (user.getFirstName() != null || user.getSecondName() != null) {
                        forthPlaceFirstName.setText(user.getFirstName());
                        forthPlaceSecondName.setText(user.getSecondName());
                        forthPlacePoints.setText(user.getUserPoints());
                    }
                }

                if (groupId.equals("5")) {
                    if (user.getFirstName() != null || user.getSecondName() != null) {
                        fifthPlaceFirstName.setText(user.getFirstName());
                        fifthPlaceSecondName.setText(user.getSecondName());
                        fifthPlacePoints.setText(user.getUserPoints());
                    }
                }

            }
        });
    }



    private void setDataToFirebase (final String groupId) {//метод отправляет данные в firebase

        firebaseFirestore = FirebaseFirestore.getInstance();

        User user = new User();


        if (groupId.equals("1")) {
            if (!firstPlaceFirstName.getText().toString().trim().equals("") || !firstPlaceSecondName.getText().toString().trim().equals("")) {
                user.setFirstName(firstPlaceFirstName.getText().toString().trim());
                user.setSecondName(firstPlaceSecondName.getText().toString().trim());
                user.setUserPoints(firstPlacePoints.getText().toString().trim());

            }
        }

        if (groupId.equals("2")) {
            if (!secondPlaceFirstName.getText().toString().trim().equals("") || !secondPlaceSecondName.getText().toString().trim().equals("")) {
                user.setFirstName(secondPlaceFirstName.getText().toString().trim());
                user.setSecondName(secondPlaceSecondName.getText().toString().trim());
                user.setUserPoints(secondPlacePoints.getText().toString().trim());
            }
        }

        if (groupId.equals("3")) {
            if (!thirdPlaceFirstName.getText().toString().trim().equals("") || !thirdPlaceSecondName.getText().toString().trim().equals("")) {
                user.setFirstName(thirdPlaceFirstName.getText().toString().trim());
                user.setSecondName(thirdPlaceSecondName.getText().toString().trim());
                user.setUserPoints(thirdPlacePoints.getText().toString().trim());
            }
        }

        if (groupId.equals("4")) {
            if (!forthPlaceFirstName.getText().toString().trim().equals("") || !forthPlaceSecondName.getText().toString().trim().equals("")) {
                user.setFirstName(forthPlaceFirstName.getText().toString().trim());
                user.setSecondName(forthPlaceSecondName.getText().toString().trim());
                user.setUserPoints(forthPlacePoints.getText().toString().trim());
            }
        }

        if (groupId.equals("5")) {
            if (!fifthPlaceFirstName.getText().toString().trim().equals("") || !fifthPlaceSecondName.getText().toString().trim().equals("")) {
                user.setFirstName(fifthPlaceFirstName.getText().toString().trim());
                user.setSecondName(fifthPlaceSecondName.getText().toString().trim());
                user.setUserPoints(fifthPlacePoints.getText().toString().trim());
            }
        }


        DocumentReference groupItemDocumentReference = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document(onGroupClick).collection(onGroupClick).document(groupId);

        groupItemDocumentReference.set(user);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}