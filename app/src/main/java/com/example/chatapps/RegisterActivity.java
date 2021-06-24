package com.example.chatapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapps.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference root;
    EditText memail,musername,mpassword;
    Button btnregister;
    TextView txt_already;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        anhxa();
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = musername.getText().toString().trim();
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                if(username.isEmpty())
                {
                    musername.setError("Không được để trống");
                    return;
                }
                if(email.isEmpty())
                {
                    memail.setError("Không được để trống");
                    return;
                }
                if(password.isEmpty())
                {
                    mpassword.setError("Không được để trống");
                    return;
                }
                if(password.length()<6)
                {
                    mpassword.setError("Mật khẩu phải nhiều hơn 6 ký tự");
                }
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            db = FirebaseDatabase.getInstance();
                            root = db.getReference();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            Users users = new Users(username,email,password,"default",userid,"offline",username.toLowerCase());
                            String id = task.getResult().getUser().getUid();
                            root.child("Users").child(id).setValue(users);

                            Toast.makeText(RegisterActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        txt_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void anhxa()
    {
        musername = (EditText)findViewById(R.id.username);
        memail = (EditText)findViewById(R.id.email);
        mpassword = (EditText)findViewById(R.id.password);
        btnregister = (Button)findViewById(R.id.btn_register);
        txt_already = (TextView)findViewById(R.id.txt_already);
    }
}