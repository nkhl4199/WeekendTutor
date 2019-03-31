package com.chetan.wt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditStudentProfile extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    String StudentID;
    Integer flag=0, flag_im=0;
    EditText name, qualification, city;
    TextView mail;
    Button back, save;
    DatabaseReference reff;
    Bitmap bitmap;
    public String downloadUrl=new String("https://i.imgur.com/tGbaZCY.jpg");
    ImageButton profiledp;
    private FirebaseAuth fa;
    ProgressDialog pd;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Students");


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RESULT_LOAD_IMAGE && resultCode== RESULT_OK && null!=data){
            bitmap=(Bitmap)data.getExtras().get("data");
            profiledp.setImageBitmap(bitmap);
            flag_im=1;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);
        setTitle("Edit Profile");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.startblue1)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StudentID =  getIntent().getStringExtra("StudentID");
        name = (EditText) findViewById(R.id.editSname);
        qualification = (EditText) findViewById(R.id.editSqualification);
        city = (EditText) findViewById(R.id.editScity);
        mail = (TextView) findViewById(R.id.editSemail);
        profiledp=(ImageButton)findViewById(R.id.dp);
        save = (Button) findViewById(R.id.save);
        reff = FirebaseDatabase.getInstance().getReference().child("Students").child(StudentID);

        pd = new ProgressDialog(this);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                mail.setText(dataSnapshot.child("mail").getValue().toString());
                qualification.setText(dataSnapshot.child("qualification").getValue().toString());
                city.setText(dataSnapshot.child("city").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final StorageReference mountainsRef = storageRef.child(StudentID+".jpg");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if( name.getText().toString().trim().length() == 0 )
                    name.setError( "Student Name is required!" );
                else
                    flag++;
                if( qualification.getText().toString().trim().length() == 0 )
                    qualification.setError( "Qualification is required!" );
                else
                    flag++;
                if( city.getText().toString().trim().length() == 0 )
                    city.setError( "Address is required!" );
                else
                    flag++;
                if (flag==3) {
                    pd.show();
                    if(flag_im==1){
                        final StorageReference students_img = mountainsRef.child(StudentID + ".jpg");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
//                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                        UploadTask uploadTask = students_img.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                students_img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUrl = uri.toString();
                                        reff.child("name").setValue(name.getText().toString().trim());
                                        reff.child("qualification").setValue(qualification.getText().toString().trim());
                                        reff.child("city").setValue(city.getText().toString().trim());
                                        reff.child("mail").setValue(mail.getText().toString().trim());
                                        reff.child("durl").setValue(downloadUrl);
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                    else {
                        reff.child("name").setValue(name.getText().toString().trim());
                        reff.child("qualification").setValue(qualification.getText().toString().trim());
                        reff.child("city").setValue(city.getText().toString().trim());
                        reff.child("mail").setValue(mail.getText().toString().trim());
                        finish();
                    }
                }
                else
                    flag=0;
            }
        });

        //Toast.makeText(this,StudentID,Toast.LENGTH_SHORT).show();
//        back = (Button)findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intobj2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intobj2,RESULT_LOAD_IMAGE);

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
