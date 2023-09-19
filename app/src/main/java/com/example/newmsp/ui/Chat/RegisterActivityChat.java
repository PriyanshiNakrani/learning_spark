package com.example.newmsp.ui.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.newmsp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivityChat extends AppCompatActivity {


    MaterialEditText et_username, et_password, et_email;
    Button registerbtn;
    Toolbar toolbar;

    String username, email, password;

    FirebaseAuth mAuth;

    DatabaseReference reference;


    SharedPreferences sharedPreferences;

    //so create a shared preference name and also create key name

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_chat);

        toolbar = findViewById(R.id.toolbarregis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        et_username = findViewById(R.id.reg_username);
        et_email = findViewById(R.id.reg_email);
        et_password = findViewById(R.id.reg_password);
        registerbtn = findViewById(R.id.register_Account_btn);

        mAuth = FirebaseAuth.getInstance();


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        //when open activity first check shared preference data available or not

        String name = sharedPreferences.getString(KEY_NAME, null);

        if (name != null) {

            Intent intent = new Intent(RegisterActivityChat.this, MainActivityChat.class);
            startActivity(intent);
            finish();
        }




        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();
                username = et_username.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME,et_username.getText().toString());
                editor.apply();

//                Intent intent = new Intent(RegisterActivityChat.this, MainActivityChat.class);
//                startActivity(intent);



                if (TextUtils.isEmpty(email)) {
                    et_email.setError("Required");
                } else if (TextUtils.isEmpty(username)) {
                    et_username.setError("Required");

                } else if (TextUtils.isEmpty(password)) {
                    et_password.setError("Required");

                } else if (password.length() < 6) {

                    et_password.setError("Length Must Be 6 or more");
                } else {

                    registerUser(username, password, email);

                }


            }
        });





    }

    private void registerUser(final String username, String password, final String email) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    reference = FirebaseDatabase.getInstance().getReference("Users_Chat").child(user.getUid());


                    if (user!=null) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", username);
                        hashMap.put("email", email);
                        hashMap.put("id", user.getUid());
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {

                                    Toast.makeText(RegisterActivityChat.this, "Now You Able To Chat", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(RegisterActivityChat.this,
                                            MainActivityChat.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));

                                }

                            }
                        });


                    }


                }


            }
        });


    }


}