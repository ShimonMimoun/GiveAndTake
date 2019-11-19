package com.example.giveandtake.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.giveandtake.Connect_Fragment;
import com.example.giveandtake.R;
import com.example.giveandtake.Start_Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Post_activity extends AppCompatActivity {
    private Button giveBtn;
    private Button takeBtn;
    private Button createPost;
    private EditText freeText;
    private String courrentName;
    private String courrentPhone;
    private String couurentGive;
    private String courrentTake;
    private String []giveOptions;
    private String []takeOptions;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String currentUserID;
    private DatabaseReference RootRef;
    private String MoreInfoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_activity);

        createPost = findViewById(R.id.createPostbtn);
        giveBtn = findViewById(R.id.giveBtn);
        takeBtn = findViewById(R.id.takeBtn);
        freeText = findViewById(R.id.freeText);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        RootRef = firebaseDatabase.getInstance().getReference();




        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPostToDataBase();
                Intent i = new Intent(Post_activity.this, Connect_Fragment.class);
                startActivity(i);
            }
        });


        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveOptions = getResources().getStringArray(R.array.Option1);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Post_activity.this);
                mBuilder.setTitle("Choose From Options:");
                mBuilder.setSingleChoiceItems(giveOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        couurentGive = giveOptions[which];
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeOptions = getResources().getStringArray(R.array.Option2);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Post_activity.this);
                mBuilder.setTitle("Choose From Options:");
                mBuilder.setSingleChoiceItems(takeOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courrentTake = takeOptions[which];
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });



    }

    public void registerPostToDataBase(){
        MoreInfoText = freeText.getText().toString().trim();
        Log.e(": TAG10=",MoreInfoText);
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                        courrentName = retrieveUserName;
                        String retrieveUserPhone = dataSnapshot.child("phone").getValue().toString();
                        courrentPhone = retrieveUserPhone;

                        Post p = new Post(R.drawable.item_24dp,courrentName,courrentPhone,couurentGive,courrentTake,MoreInfoText,currentUserID);
                        Log.e(": TAG5=",courrentName+" "+courrentPhone+" "+couurentGive+" "+courrentTake+" "+MoreInfoText+" "+currentUserID);

                        String id = RootRef.push().getKey();
                        FirebaseDatabase.getInstance().getReference("Posts").child(id).setValue(p);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

    }



    }

