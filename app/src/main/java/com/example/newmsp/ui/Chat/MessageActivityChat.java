package com.example.newmsp.ui.Chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newmsp.adapter.MessageAdapterChat;
import com.example.newmsp.R;
import com.example.newmsp.model.Chats;
import com.example.newmsp.model.Users_Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivityChat extends AppCompatActivity {


    String friendid, message, myid;
    CircleImageView imageViewOnToolbar;
    TextView usernameonToolbar;
    Toolbar toolbar;
    FirebaseUser firebaseUser;

    EditText et_message;
    Button send;

    DatabaseReference reference;

    List<Chats> chatsList;
    MessageAdapterChat messageAdapter;
    RecyclerView recyclerView;
    ValueEventListener seenlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewOnToolbar = findViewById(R.id.profile_image_toolbar_message);
        usernameonToolbar = findViewById(R.id.username_ontoolbar_message);

        recyclerView = findViewById(R.id.recyclerview_messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        send = findViewById(R.id.send_messsage_btn);
        et_message = findViewById(R.id.edit_message_text);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myid = firebaseUser.getUid();

        friendid = getIntent().getStringExtra("friendid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_Chat").child(friendid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users_Chat users_chat = snapshot.getValue(Users_Chat.class);

                usernameonToolbar.setText(users_chat.getUsername());

                if (Objects.equals(users_chat.getImageURL(), "default")) {

                    imageViewOnToolbar.setImageResource(R.drawable.user);
                } else {

                    Glide.with(getApplicationContext()).load(users_chat.getImageURL()).into(imageViewOnToolbar);
                }

                readMessages(myid, friendid, users_chat.getImageURL());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        seenMessage(friendid);






        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().length() > 0) {

                    send.setEnabled(true);

                } else {

                    send.setEnabled(false);


                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = et_message.getText().toString();

                if (!text.startsWith(" ")) {
                    et_message.getText().insert(0, " ");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                message = et_message.getText().toString();

                sendMessage(myid, friendid, message);

                et_message.setText(" ");


            }
        });




    }



    private void seenMessage(final String friendid) {

        reference = FirebaseDatabase.getInstance().getReference("Chats");


        seenlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);

                    if (Objects.equals(chats.getReciever(), myid) && Objects.equals(chats.getSender(), friendid)) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        ds.getRef().updateChildren(hashMap);

                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void readMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);

                    if (Objects.equals(chats.getSender(), myid) && Objects.equals(chats.getReciever(), friendid) ||
                            Objects.equals(chats.getSender(), friendid) && Objects.equals(chats.getReciever(), myid)) {

                        chatsList.add(chats);
                    }

                    messageAdapter = new MessageAdapterChat(MessageActivityChat.this, chatsList, imageURL);
                    recyclerView.setAdapter(messageAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(final String myid, final String friendid, final String message) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();



                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sender", myid);
                hashMap.put("reciever", friendid);
                hashMap.put("message", message);
                hashMap.put("isseen", false);

                reference.child("Chats").push().setValue(hashMap);


                final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chatslist").child(myid).child(friendid);

                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (!snapshot.exists()) {


                            reference1.child("id").setValue(friendid);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }




    private void Status (final String status) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_Chat").child(firebaseUser.getUid());

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", status);

                reference.updateChildren(hashMap);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
        reference.removeEventListener(seenlistener);
    }


}