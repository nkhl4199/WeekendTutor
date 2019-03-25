package com.chetan.wt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseDetails extends AppCompatActivity {
    TextView name, agenda, date, time, duration, venue, tname;
    Button delete;
    String cid, tid;
    Intent in;
    String sid;
    int student_wallet,tutor_wallet,price;

    DatabaseReference reff, notify, temp, tutor;
    TutorNotification notification = new TutorNotification();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        setTitle("Course Details");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.startblue1)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        name = (TextView)findViewById(R.id.Cname);
        tname = (TextView)findViewById(R.id.Tname);
        agenda = (TextView)findViewById(R.id.Cagenda);
        date = (TextView)findViewById(R.id.Cdate);
        time = (TextView)findViewById(R.id.Ctime);
        duration = (TextView)findViewById(R.id.Cduration);
        venue = (TextView)findViewById(R.id.Cvenue);
        cid = getIntent().getStringExtra("CourseID");
        //Toast.makeText(this,ID,Toast.LENGTH_SHORT).show();


        final int[] total_std = new int[1];

        final int[] count = new int[1];

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sid = user.getUid();

        temp = FirebaseDatabase.getInstance().getReference("Students");
        temp.child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").getValue()!=null)
                notification.setStudent_name(dataSnapshot.child("name").getValue().toString()+" unregistered for ");
                student_wallet = Integer.parseInt(dataSnapshot.child("wallet").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        reff = FirebaseDatabase.getInstance().getReference("Student Courses").child(sid).child(cid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0) {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    notification.setCourse_name(dataSnapshot.child("name").getValue().toString()+ " which is on ");
                    agenda.setText(dataSnapshot.child("agenda").getValue().toString());
                    date.setText(dataSnapshot.child("date").getValue().toString());
                    notification.setDate(dataSnapshot.child("date").getValue().toString());
                    tname.setText(dataSnapshot.child("tname").getValue().toString());
                    time.setText(dataSnapshot.child("start").getValue().toString());
                    duration.setText(dataSnapshot.child("duration").getValue().toString());
                    venue.setText(dataSnapshot.child("venue").getValue().toString());
                    tid=dataSnapshot.child("tid").getValue().toString();
                  //  count[0] = Integer.parseInt(dataSnapshot.child("no_of_students").getValue().toString());
                    price = Integer.parseInt(dataSnapshot.child("price").getValue().toString());

                    tutor = FirebaseDatabase.getInstance().getReference("users").child(tid);
                    tutor.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tutor_wallet = Integer.parseInt(dataSnapshot.child("wallet").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else
                    finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        delete = (Button)findViewById(R.id.delete);

        final DatabaseReference tu_ref = FirebaseDatabase.getInstance().getReference("Tutor Courses").child(cid);
        tu_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count[0] = Integer.parseInt(dataSnapshot.child("no_of_students").getValue().toString());
               // Toast.makeText(getApplicationContext(),String.valueOf(count[0]),Toast.LENGTH_LONG).show();
                count[0]--;
                //tut.child("no_of_students").setValue(count[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//       Toast.makeText(getApplicationContext(),Integer.toString(price),Toast.LENGTH_SHORT).show();



        final DatabaseReference tut = FirebaseDatabase.getInstance().getReference("Tutor Courses").child(cid);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetails.this);

                builder.setMessage("Are you sure you want to Delete this Course?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notify = FirebaseDatabase.getInstance().getReference("Tutor Notifications").child(tid);
                        notify.push().setValue(notification);

                        DatabaseReference del = FirebaseDatabase.getInstance().getReference("Student Courses").child(sid).child(cid);
                     //  count[0]--;
                        tut.child("no_of_students").setValue(count[0]);
                        //DatabaseReference tu_wallet = FirebaseDatabase.getInstance().getReference("users").child(tid);
                        int t1=tutor_wallet-price+10;
                        tutor.child("wallet").setValue(t1);
                        int t2= student_wallet+price-10;
                        temp.child(sid).child("wallet").setValue(t2);
                        del.setValue(null);
                        Toast.makeText(getApplicationContext(),"Deleted Succesfully",Toast.LENGTH_SHORT).show();
                        Intent in =new Intent(getApplicationContext(),CourseList.class);
                        startActivity(in);
                    }
                }).setNegativeButton("Cancel",null);
                AlertDialog alert = builder.create();
                alert.show();
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
