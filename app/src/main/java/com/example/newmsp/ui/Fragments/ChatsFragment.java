package com.example.newmsp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmsp.adapter.UserAdapterChat;
import com.example.newmsp.model.Chatslist;
import com.example.newmsp.model.Users_Chat;
import com.example.newmsp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {


    List<Chatslist> userlist;
    List<Users_Chat> mUsers;
    RecyclerView recyclerView;
    UserAdapterChat mAdapter;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_chats, container, false);


        userlist = new ArrayList<>();

        recyclerView = view.findViewById(R.id.chat_recyclerview_chatfrag);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Chatslist")
                .child(firebaseUser.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userlist.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Chatslist chatslist = ds.getValue(Chatslist.class);

                    userlist.add(chatslist);



                }

                ChatsListings();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }

    private void ChatsListings() {

        mUsers = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_Chat");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUsers.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Users_Chat users = ds.getValue(Users_Chat.class);

                    for (Chatslist chatslist: userlist) {


                        if (users.getId().equals(chatslist.getId())) {


                            mUsers.add(users);




                        }




                    }


                }

                mAdapter = new UserAdapterChat(getContext(), mUsers, true);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}