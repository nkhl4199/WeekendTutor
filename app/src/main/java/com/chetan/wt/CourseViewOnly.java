package com.chetan.wt;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CourseViewOnly extends AppCompatActivity {

    TextView coursename,tutorName,Venue,Time,Duration,courseAgenda,course_date,price;
    String course_name,tutor_name,venue,time,duration,courseagenda,coursedate,pr;
    Button Edit;
    String id,TId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view_only);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        coursename = (TextView) findViewById(R.id.courseName);
        coursename.setText(getIntent().getStringExtra("Course_name"));


        id = getIntent().getStringExtra("CourseID");
        tutorName = (TextView) findViewById(R.id.tutorName);
        tutorName.setText(getIntent().getStringExtra("Tutor_name"));

        Venue = (TextView) findViewById(R.id.Venue);
        Venue.setText(getIntent().getStringExtra("Venue"));

        Time = (TextView) findViewById(R.id.Time);
        Time.setText(getIntent().getStringExtra("Time"));

        Duration = (TextView) findViewById(R.id.Duration);
        Duration.setText(getIntent().getStringExtra("Duration"));

        courseAgenda = (TextView) this.findViewById(R.id.courseAgenda);
        courseAgenda.setText(getIntent().getStringExtra("agenda"));

        course_date = (TextView) findViewById(R.id.course_date);
        course_date.setText(getIntent().getStringExtra("date"));

        price = findViewById(R.id.price);
        final int value = getIntent().getIntExtra("price",0);
        price.setText(String.valueOf(value));


        TId = getIntent().getStringExtra("TId");

        Edit = (Button)findViewById(R.id.editCourse);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(CourseViewOnly.this,CourseView.class);

                intent.putExtra("CourseID",id);
                // Toast.makeText(MainActivity.this,Integer.toString(course_list.size()),Toast.LENGTH_SHORT).show();
                intent.putExtra("Course_name", getIntent().getStringExtra("Course_name"));
                intent.putExtra("Tutor_name", getIntent().getStringExtra("Tutor_name"));
                intent.putExtra("Venue",getIntent().getStringExtra("Venue"));
                intent.putExtra("Time", getIntent().getStringExtra("Time"));
                intent.putExtra("Duration", getIntent().getStringExtra("Duration"));
                intent.putExtra("agenda", getIntent().getStringExtra("agenda"));
                intent.putExtra("date", getIntent().getStringExtra("date"));
                intent.putExtra("TId",getIntent().getStringExtra("TId"));
                intent.putExtra("price",String.valueOf(value));
                //intent.putExtra("id", mCourse.getI());
                startActivity(intent);
                finish();



            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
