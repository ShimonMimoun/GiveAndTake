    package com.android.giveandtake.Login;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.android.giveandtake.Admin.AdminConnect;
    import com.android.giveandtake.Connect_Fragment;
    import com.android.giveandtake.R;
    import com.android.giveandtake.Start_Application;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;

    public class LoginActivity extends AppCompatActivity {
        private EditText emailboxLogin;

    private  EditText passwordboxLogin;
    private  Button buttonLogin;
    private  Button ReturnBtn, forgotPassword;

    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordboxLogin = (EditText)findViewById(R.id.passwordboxLogin);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        firebaseAuth = firebaseAuth.getInstance();
        ReturnBtn = (Button)findViewById(R.id.returnLoginbtn);
        forgotPassword = (Button)findViewById(R.id.forgotpassword);

        ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Start_Application.class);
                startActivity(i);
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent forgot=new Intent(LoginActivity.this,Forgot_code.class);
        startActivity(forgot);
            }
        });




        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Processing...", true);
                (firebaseAuth.signInWithEmailAndPassword(emailboxLogin.getText().toString(), passwordboxLogin.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    if(firebaseAuth.getCurrentUser().getEmail().equals("giveandtake.contacts@gmail.com")){

                                        startActivity(new Intent (LoginActivity.this, AdminConnect.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        Toast.makeText(LoginActivity.this, "Administrator Connector", Toast.LENGTH_LONG).show();
finish();



                                    }else {


                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(LoginActivity.this, Connect_Fragment.class);
                                        i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    Log.e("onComplete: Failed=", task.getException().getMessage());
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });



    }
}