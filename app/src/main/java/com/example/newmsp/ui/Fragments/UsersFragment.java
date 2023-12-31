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
import java.util.Objects;

public class UsersFragment extends Fragment {


    RecyclerView recyclerView;
    List<Users_Chat> usersList;
    UserAdapterChat mAdapter;
    FirebaseUser firebaseUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_users_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        displayusers();

        return view;
    }

    private void displayusers() {

        usersList = new ArrayList<>();

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Users_Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Users_Chat users = ds.getValue(Users_Chat.class);

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (!Objects.equals(users.getId(), firebaseUser.getUid())) {


                        usersList.add(users);

                    }


                    mAdapter  = new UserAdapterChat(getContext(), usersList, false);
                    recyclerView.setAdapter(mAdapter);








                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}