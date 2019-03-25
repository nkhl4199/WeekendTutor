package com.chetan.wt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerStudent extends AppCompatActivity {

    private int flag=0;
    private FirebaseAuth mAuth;
    DatabaseReference dataBase;
    ImageButton profiledp;
    private static int RESULT_LOAD_IMAGE = 1;
    private FirebaseAuth fa;
    Bitmap bitmap;
    private ProgressDialog pb;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RESULT_LOAD_IMAGE && resultCode== RESULT_OK && null!=data){
            bitmap=(Bitmap)data.getExtras().get("data");
            profiledp.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        Intent intent = getIntent();
        fa=FirebaseAuth.getInstance();


        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference().child("Students");
        final Button register =(Button)findViewById(R.id.registerbutton);
        profiledp = (ImageButton)findViewById(R.id.stuDp);
        pb=new ProgressDialog(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                final Intent Newpage = new Intent(registerStudent.this, loginStudent.class);

                final TextView name = (TextView) findViewById(R.id.name);
                final TextView mail = (TextView) findViewById(R.id.email);
                final TextView pass = (TextView) findViewById(R.id.password);
                final TextView pass_cpy = (TextView) findViewById(R.id.confirmpassword);
                final TextView city = (TextView) findViewById(R.id.city);
                final TextView qualification = (TextView) findViewById(R.id.qualification);


                final String Name = name.getText().toString();
                final String Mail = mail.getText().toString();
                final String Pass = pass.getText().toString();
                final String Cpy_Pass = pass_cpy.getText().toString();
                final String City = city.getText().toString();
                final String Quali = qualification.getText().toString();

                if (TextUtils.isEmpty(Name))
                {
                    name.setError("Enter name!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Mail))
                {
                    mail.setError("Enter email!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Pass))
                {
                    pass.setError("Enter Password!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Cpy_Pass))
                {
                    pass.setError("Confirm Password!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(City))
                {
                    pass.setError("Enter City!!");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Quali))
                {
                    pass.setError("Enter Qualification!!");
                    flag = 1;
                }


                String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                final Matcher matcher = pattern.matcher(Mail);

                Pattern name_pattern = Pattern.compile("[A-Za-z ]+");
                final Matcher name_matcher = name_pattern.matcher(Name);


                if(flag == 0) {
                    //Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_LONG).show();
                    mAuth.createUserWithEmailAndPassword(Mail, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                students user;
                                user = new students();
                                user.setWallet(500);
                                user.setName(Name);
                                user.setMail(Mail);
                                user.setQualification(Quali);
                                user.setCity(City);
                                FirebaseUser cuser = fa.getCurrentUser();
                                String id = cuser.getUid();
                                dataBase.child(id).setValue(user);
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                startActivity(Newpage);
                            }
                            else {
                                    if(!name_matcher.matches())
                                    {
                                        name.setError("Invalid Name!!");
                                        flag++;
                                    }
                                    if(!matcher.matches()) {
                                        mail.setError("Invalid E-mail");
                                        flag++;
                                    }
                                    if(Pass.length() < 8){
                                        pass.setError("Password Not Long Enough");
                                        flag++;
                                    }
                                    if(!Pass.equals(Cpy_Pass)) {
                                        pass_cpy.setError("Password Mismatch");
                                        flag++;
                                    }
                                    Toast.makeText(getApplicationContext(), "Registration could not be completed!\nPlease Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intobj2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intobj2, RESULT_LOAD_IMAGE);
            }
        });


    }



}
