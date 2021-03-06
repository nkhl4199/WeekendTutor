package com.chetan.wt;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeAuthInfoTutor extends AppCompatActivity {

    String sid;
    FirebaseUser user;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_auth_info_tutor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.startblue1)));
        setTitle("Change Email or Password");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        sid = user.getUid();


        pd = new ProgressDialog(this);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");

        final EditText mail = (EditText)findViewById(R.id.email);
        final EditText cpass = (EditText)findViewById(R.id.cpass);
        final EditText cepass = (EditText)findViewById(R.id.cepass);
        final EditText npass = (EditText)findViewById(R.id.npass);

        Button changeEmail = (Button) findViewById(R.id.changeEmail);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), cepass.getText().toString());
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updateEmail(mail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),"Email Changed Successfully",Toast.LENGTH_SHORT).show();
                                                DatabaseReference ref = FirebaseDatabase.getInstance()
                                                        .getReference("users").child(user.getUid());
                                                ref.child("email").setValue(mail.getText().toString());
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
                                }
                                pd.dismiss();
                            }
                        });


            }
        });



        Button changePass = (Button)findViewById(R.id.changePass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pd.show();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), cpass.getText().toString());
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(npass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(getApplicationContext(),"unsuccessful",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {

                                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                }
                                pd.dismiss();
                            }
                        });

            }
        });

       /* Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

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
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
