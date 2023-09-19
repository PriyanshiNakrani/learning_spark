package com.example.newmsp.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newmsp.Constant;
import com.example.newmsp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class t_medium extends AppCompatActivity {

    MaterialCardView cardE, cardG, cardH;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_ROOT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_medium);

        cardE = findViewById(R.id.cardEN);
        cardG = findViewById(R.id.cardGU);
        cardH = findViewById(R.id.cardHI);


        cardE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(t_medium.this,t_std_e.class);
               i.putExtra(Constant.MEDIUM, Constant.M_ENGLISH);
               startActivity(i);
               finish();
            }
        });

        cardG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(t_medium.this,t_std_e.class);
                i.putExtra(Constant.MEDIUM, Constant.M_GUJARATI);
                startActivity(i);
                finish();
            }
        });

        cardH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(t_medium.this,t_std_e.class);
                i.putExtra(Constant.MEDIUM, Constant.M_HINDI);
                startActivity(i);
                finish();

                goToNext();
            }
        });
    }


    private void goToNext() {

    }


}