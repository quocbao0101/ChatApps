package com.example.chatapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    EditText edit_reset;
    Button btn_reset_password;
    Button btn_huy;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar tool_bar = findViewById(R.id.bar_layout);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setTitle("Đặt lại mật khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhxa();
        firebaseAuth = FirebaseAuth.getInstance();
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassword.this,LoginActivity.class));
            }
        });
        btn_reset_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String edt_reset = edit_reset.getText().toString();
                if (edt_reset == "")
                {
                    Toast.makeText(ResetPassword.this, "Vui lòng không được để trống", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(edt_reset).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPassword.this,"Vui lòng kiểm tra hộp thư email của bạn",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPassword.this,LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ResetPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void anhxa()
    {
        btn_huy = (Button)findViewById(R.id.btn_huy_password);
        edit_reset = (EditText)findViewById(R.id.edit_reset);
        btn_reset_password = (Button)findViewById(R.id.btn_reset_password);
    }
}