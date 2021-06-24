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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText lg_email,lg_password;
    private Button btnlogin;
    private TextView forget_password;
    private TextView txtalreadylg;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        firebaseAuth = FirebaseAuth.getInstance();
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResetPassword.class);
                startActivity(intent);
            }
        });
        txtalreadylg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtemail = lg_email.getText().toString();
                String txtpassword = lg_password.getText().toString();
                if(txtemail.isEmpty() || txtpassword.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Vui lòng không được để trống",Toast.LENGTH_SHORT).show();
                }
                else
                    firebaseAuth.signInWithEmailAndPassword(txtemail,txtpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });
    }
    public void anhxa()
    {
        btnlogin = (Button) findViewById(R.id.btn_login);
        lg_password = (EditText)findViewById(R.id.login_password);
        lg_email = (EditText)findViewById(R.id.login_email);
        txtalreadylg = (TextView)findViewById(R.id.login_txt_already);
        forget_password =(TextView)findViewById(R.id.forget_password);
    }
}