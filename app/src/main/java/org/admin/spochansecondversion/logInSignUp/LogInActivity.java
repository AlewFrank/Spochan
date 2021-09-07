package org.admin.spochansecondversion.logInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.admin.spochansecondversion.News.NewsActivity;
import org.admin.spochansecondversion.R;

import org.admin.spochansecondversion.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {









    //если решишь задавать тренеров и админов в приложении, то просто вот сегодня 23.02.2021, открой последнюю сохраненку до сегодня и посмотри, что тут было, потому что то, что тут до удаления некоторого кода, нихрена не работает


















    private static final String TAG = "LogInActivity";

    private Button logInButton, arrowBackButton;
    TextView toggleLoginSingUpTextView, forgetPassword, sentEmailButton, backButton, signUpButton;
    TextInputLayout textInputEmail;
    TextInputLayout textInputFirstName;
    TextInputLayout textInputSecondName;
    TextInputLayout textInputPassword;
    TextInputLayout textInputConfirmPassword;

    public boolean isDirectorModeActivated = false;
    private boolean isLoginModeActive = true;//по умолчанию все boolean переменные false

    private FirebaseAuth auth;

    private DatabaseReference mDatabase;//для работы с realtime database, изначально использовали, решид оставить для примера
    private FirebaseDatabase database;

    private FirebaseFirestore firebaseFirestore;//для работы с cloud firestore


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        //устанавливаем специальный шрифт, который находится при выборе сверху слева Project, далее app/src/main/assets/fonts
        forgetPassword = findViewById(R.id.forgetPassword);
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-LightItalic.ttf");
        forgetPassword.setTypeface(roboto);
        

        logInButton = findViewById(R.id.logInButton);
        signUpButton = findViewById(R.id.signUpButton);
        arrowBackButton = findViewById(R.id.arrowBackButton);
        sentEmailButton = findViewById(R.id.sentEmailButton);
        backButton = findViewById(R.id.backButton);
        //toggleLoginSingUpTextView = findViewById(R.id.toggleLoginSingUpTextView);
        //helloTextView = findViewById(R.id.helloTextView);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputFirstName = findViewById(R.id.textInputFirstName);
        textInputSecondName = findViewById(R.id.textInputSecondName);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);

        textInputFirstName.setVisibility(View.GONE);
        textInputSecondName.setVisibility(View.GONE);
        textInputConfirmPassword.setVisibility(View.GONE);
        arrowBackButton.setVisibility(View.GONE);
        sentEmailButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        //это для работы с Realtime Database, мы же сейчас изменим на Cloud firestore, чтоб удобнее было рейтинг создавать, но это оставил, чтоб потом может когда-то подсмотреть
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //database = FirebaseDatabase.getInstance();//получаем доступ к корневой папке нашей базы данных

       if (auth.getCurrentUser() != null) {//если пользователь уже авторизован
            startActivity(new Intent(LogInActivity.this, NewsActivity.class));
        }
    }

    private boolean validateEmail() {

        String emailInput = textInputEmail.getEditText().getText().toString().trim();//получаем то, что человек ввел

        if (emailInput.isEmpty()) {
            textInputEmail.setError(getResources().getString(R.string.set_email));
            return false;
        } else {
            textInputEmail.setError("");//убираем сообщение об ошибке, если человек с первого раза ошибся и ему высветилась ошибка
            return true;
        }
    }

    private boolean validateFirstName() {

        String firstNameInput = textInputFirstName.getEditText().getText().toString()
                .trim();

        if (firstNameInput.isEmpty()) {
            textInputFirstName.setError(getResources().getString(R.string.set_first_name));
            return false;
        }  else {
            textInputFirstName.setError("");
            return true;
        }
    }

    private boolean validateSecondName() {

        String secondNameInput = textInputSecondName.getEditText().getText().toString()
                .trim();

        if (secondNameInput.isEmpty()) {
            textInputSecondName.setError(getResources().getString(R.string.set_second_name));
            return false;
        } else {
            textInputSecondName.setError("");
            return true;
        }
    }

    private boolean validatePassword() {

        String passwordInput = textInputPassword.getEditText().getText()
                .toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError(getResources().getString(R.string.set_password));
            return false;
        } else if (passwordInput.length() < 6) {
            textInputPassword.setError(getResources().getString(R.string.normal_password_length));
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }
    }

    private boolean validateConfirmPassword() {

        String passwordInput = textInputPassword.getEditText().getText()
                .toString().trim();
        String confirmPasswordInput = textInputConfirmPassword.getEditText().getText()
                .toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            textInputConfirmPassword.setError(getResources().getString(R.string.password_match));
            return false;
        } else {
            textInputConfirmPassword.setError("");
            return true;
        }
    }


    public void toggleLoginMode(View view) {//онклик метод синей надписи под кнопкой

        if (!isLoginModeActive){
            isLoginModeActive = true;
            logInButton.setText(getResources().getString(R.string.enter));
            toggleLoginSingUpTextView.setText(getResources().getString(R.string.no_account));
            textInputFirstName.setVisibility(View.GONE);
            textInputSecondName.setVisibility(View.GONE);
            textInputConfirmPassword.setVisibility(View.GONE);
        } else {
            isLoginModeActive = false;
            logInButton.setText(getResources().getString(R.string.register));
            toggleLoginSingUpTextView.setText(getResources().getString(R.string.have_account));
            textInputFirstName.setVisibility(View.VISIBLE);
            textInputSecondName.setVisibility(View.VISIBLE);
            textInputConfirmPassword.setVisibility(View.VISIBLE);
        }
    }

    public void loginSignUpUser(View view) {//онклик метод из нашей разметки

        if (!validateEmail() | !validatePassword()) {// такая штука | обозначает почти как "или" (две вертикальных), но при этом проверяет все условия, а не ищет первое, которое совпадает и на остальные забивает. С двумя палками у нас бы выводилось только одно сообщение об ошибке для того поле ввода, которое первое попадет на проверку, а с двумя палками выводится сразу все сообщения об ошибке, в тех полях, где они есть
                return;
            }


        if (isLoginModeActive) {
            Log.d("Fuck", "Логин");
            auth.signInWithEmailAndPassword(textInputEmail.getEditText().getText().toString().trim(), textInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                startActivity(new Intent(LogInActivity.this, NewsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                // ...
                            }
                            // ...
                        }
                    });

        } else {
            if (!validateEmail() | !validateFirstName() | !validateSecondName() | !validatePassword() | !validateConfirmPassword()) {// такая штука | обозначает почти как или (две вертикальных), но при этом проверяет все условия, а не ищет первое, которое совпадает и на остальные забивает. С двумя палками у нас бы выводилось только одно сообщение об ошибке для того поле ввода, которое первое попадет на проверку, а с двумя палками выводится сразу все сообщения об ошибке, в тех полях, где они есть
                    return;
                }

            Log.d("Fuck", "Регистрация");


            auth.createUserWithEmailAndPassword(textInputEmail.getEditText().getText().toString().trim(), textInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser currentUser = auth.getCurrentUser();
                                createUser(currentUser);
                                startActivity(new Intent(LogInActivity.this, NewsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }

    }

    private void createUser(FirebaseUser firebaseUser) {//создает пользователя в в firebase firestore

        firebaseFirestore = FirebaseFirestore.getInstance();

        User user = new User();
        user.setId(firebaseUser.getUid());//это поле и ниже получаем из облачной базы данных(вводим эти значения при аутентификации)
        user.setEmail(firebaseUser.getEmail());
        user.setFirstName(textInputFirstName.getEditText().getText().toString().trim());//это поле берем из edit text, в которое записывали имя при регистрации и после отправляем в баз данных
        user.setSecondName(textInputSecondName.getEditText().getText().toString().trim());
        user.setDaysBornDate("");//если не укажем нулевые значения, то в MyProfile будет вылетать приложение, так как будет пытаться получить переменную DaysBornDate, а ее впринципе не существует
        user.setMonthBornDate("");
        user.setYearBornDate("");
        user.setAvatarUrl("");
        user.setSex(getResources().getString(R.string.gender_not_stated));
        user.setDirector(isDirectorModeActivated);
        user.setUserPoints("");

        String currentUserUid = firebaseUser.getUid();

        firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid).set(user);

        //это для работы с Realtime Database, мы же сейчас изменим на Cloud firestore, чтоб удобнее было рейтинг создавать
        //mDatabase.child(getResources().getString(R.string.app_country)).child("Users").child(currentUserUid).setValue(user);//этот метод из документации Firebase, благодаря нему ссылка профиля и id это одно и тоже значение, а значит гораздо легче в дальнейшем менять значения и просто работать с данными
    }

    public void forgetPassword(View view) { //онклик метод, если человек забыл свой пароль

        arrowBackButton.setVisibility(View.VISIBLE);

        //убираем все поля, кроме ввода имейла
        textInputFirstName.setVisibility(View.GONE);
        textInputPassword.setVisibility(View.GONE);
        textInputSecondName.setVisibility(View.GONE);
        textInputConfirmPassword.setVisibility(View.GONE);
        sentEmailButton.setVisibility(View.VISIBLE);
        logInButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        //toggleLoginSingUpTextView.setVisibility(View.GONE);
        Toast.makeText(LogInActivity.this, getResources().getString(R.string.input_email), Toast.LENGTH_LONG).show();
    }

    private void showDialog(String emailAddress) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//в скобках активити в которой будет появляться этот диалог
        builder.setMessage(getResources().getString(R.string.text_password) + " " + emailAddress);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){dialog.dismiss();}  //просто убираем сообщение с экрана
                startActivity(new Intent(LogInActivity.this, LogInActivity.class));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void arrowBackButton(View view) { //при нажатии на стрелочку слева-сверху
        makeTheStart();
    }

    public void sentEmail(View view) {
        final String emailAddress = textInputEmail.getEditText().getText().toString().trim();

        if (validateEmail()) {
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                showDialog(emailAddress);
                            }
                        }
                    });
        } else {
            Toast.makeText(LogInActivity.this, getResources().getString(R.string.input_email), Toast.LENGTH_LONG).show();
        }
    }

    public void signUpButton(View view) {
        isLoginModeActive = false;

        textInputFirstName.setVisibility(View.VISIBLE);
        textInputSecondName.setVisibility(View.VISIBLE);
        textInputConfirmPassword.setVisibility(View.VISIBLE);
        textInputPassword.setVisibility(View.VISIBLE);
        textInputEmail.setVisibility(View.VISIBLE);

        signUpButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        arrowBackButton.setVisibility(View.VISIBLE);

        logInButton.setText("Зарегистрироваться");
    }

    public void backButton(View view) { //назад, если была открыта регистрация
        makeTheStart();
    }

    private void makeTheStart() {
        textInputFirstName.setVisibility(View.GONE);
        textInputSecondName.setVisibility(View.GONE);
        textInputConfirmPassword.setVisibility(View.GONE);
        arrowBackButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        textInputPassword.setVisibility(View.VISIBLE);
        textInputEmail.setVisibility(View.VISIBLE);

        sentEmailButton.setVisibility(View.GONE);
        logInButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);

        logInButton.setText("Войти");

        isLoginModeActive = true;
    }
}