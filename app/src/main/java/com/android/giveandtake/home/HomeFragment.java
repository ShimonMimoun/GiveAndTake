package com.android.giveandtake.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.giveandtake.Center.Trade;
import com.android.giveandtake.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView _RecyclerView;
    private ItemAdapter _Adapter;
    private RecyclerView.LayoutManager _LayoutManager;
    private FirebaseDatabase firebaseDatabase;
    static private ArrayList<Post> PostsList;
    private DatabaseReference RootRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private Button addItem;
    private View root;
    private Button giveBtn;
    private Button takeBtn;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private String name;
    private String phone;
    private String city;

    /// get the Buttoms ////


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Posts");
        RootRef = firebaseDatabase.getInstance().getReference();

        firebaseAuth = firebaseAuth.getInstance();
        //////////////////// Create Dialog ///////////////////
        addItem = root.findViewById(R.id.addItem);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        giveBtn = root.findViewById(R.id.giveBtn);
        takeBtn = root.findViewById(R.id.takeBtn);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Post_activity.class);
                startActivity(i);
               // openDialog();
            }

        });

        createToShowPosts();

        return root;

    }

//    public void buildRecyclerView(){
//
//
//    }

    public void DeletePost(String uid) {
        //myRef = firebaseDatabase.getReference("Posts");
        myRef.child(uid).orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                dataSnapshot.getRef().removeValue();
                updateView();
                createToShowPosts();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }



    public void createToShowPosts(){


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostsList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = ds.child("nameAsk").getValue(String.class);
                    phone = ds.child("phoneAsk").getValue(String.class);
                    city = ds.child("city").getValue(String.class);
                    String give = ds.child("give").getValue(String.class);
                    String take = ds.child("take").getValue(String.class);
                    String freeText = ds.child("freeText").getValue(String.class);
                    String courrentUser = ds.child("currentUserID").getValue(String.class);
                    String PostID = ds.child("postid").getValue(String.class);




                    Post p = new Post(R.drawable.item_24dp, name,phone,city,give,take,freeText,courrentUser,PostID);
                    if(!PostsList.contains(p)){
                        PostsList.add(p);
                    }
                }

                updateView();

                _Adapter.setOnPostClickListener(new ItemAdapter.OnPostClickListener() {
                    @Override
                    public void onPostClick(final int position) {
                        PostsList.get(position);

                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setTitle("Post ID: "+PostsList.get(position).getPostid());
                        mBuilder.setMessage(PostsList.get(position).getfreeText()+"\n"+PostsList.get(position).getGive());
                        Log.e(": TAG7=",PostsList.get(position).getcurrentUserID()+" "+currentUserID);

                        if(PostsList.get(position).getcurrentUserID().equals(currentUserID)){

                            mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeletePost(PostsList.get(position).getPostid());
                                    updateView();
                                }
                            });
                            mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }else{
                            mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }

                            });
                            mBuilder.setNegativeButton("Trade", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                        RootRef.child("Users").child(currentUserID)
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot ds) {

                                                        name = ds.child("name").getValue(String.class);
                                                        phone = ds.child("phone").getValue(String.class);
                                                        city = ds.child("city").getValue(String.class);
                                                        final Trade trade = new Trade(
                                                                R.drawable.black2people,
                                                                PostsList.get(position).getPostid(),
                                                                currentUserID,
                                                                PostsList.get(position).getcurrentUserID(),
                                                                PostsList.get(position).getNameAsk(),
                                                                name,
                                                                PostsList.get(position).getGive(),
                                                                PostsList.get(position).getTake(),
                                                                phone,
                                                                city);
                                                        String postId = RootRef.push().getKey();

                                                        FirebaseDatabase.getInstance().getReference("Trades").child(postId).setValue(trade);
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }

                                                });

                                }
                            });
                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                        }



                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addListenerForSingleValueEvent(eventListener);

    }

    public void updateView(){
        _RecyclerView = root.findViewById(R.id.recyclerview);
        _RecyclerView.setHasFixedSize(true);
        _LayoutManager = new LinearLayoutManager(getContext());
        _Adapter = new ItemAdapter(PostsList);
        _RecyclerView.setLayoutManager(_LayoutManager);
        _RecyclerView.setAdapter(_Adapter);
    }

}
