package com.chetan.wt;



import android.content.Intent;

import android.graphics.drawable.ColorDrawable;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.MenuItem;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;



import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;



public class MyProfileStudent extends AppCompatActivity {

    TextView name, email, address, contact;

    String StudentID="1";

    DatabaseReference reff;

    private FirebaseAuth fa;

    String id;

    Button mbutton;

    de.hdodenhof.circleimageview.CircleImageView pro;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile_student);

        setTitle("My Profile");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.startblue1)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        pro=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profilephoto);

        mbutton = findViewById(R.id.edit);

        name = (TextView) findViewById(R.id.editSname);

        contact = (TextView) findViewById(R.id.qualification);

        address = (TextView) findViewById(R.id.location);

        email = (TextView) findViewById(R.id.email);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        final StorageReference mountainsRef = storageRef.child("Students").child(id+".jpg");

        reff = FirebaseDatabase.getInstance().getReference("Students");

        StudentID = user.getUid();



        reff.child(user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name.setText(dataSnapshot.child("name").getValue().toString());

                email.setText(dataSnapshot.child("mail").getValue().toString());

                contact.setText(dataSnapshot.child("qualification").getValue().toString());

                address.setText(dataSnapshot.child("city").getValue().toString());

                String imageUri = dataSnapshot.child("durl").getValue().toString();

                if(imageUri!="")

                    Picasso.get().load(imageUri).fit().centerCrop().into(pro);



            }



            @Override

            public void onCancelled(@NonNull DatabaseError databaseError) {



            }

        });





        mbutton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent i = new Intent(v.getContext(),EditStudentProfile.class);

                i.putExtra("StudentID",StudentID);

                startActivity(i);

            }

        });







        Button auth = (Button) findViewById(R.id.changeAuth);

        auth.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent i = new Intent(v.getContext(),ChangeAuthInfo.class);

                startActivity(i);

            }

        });





    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:



                // app icon in action bar clicked; goto parent activity.

                this.finish();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }

}